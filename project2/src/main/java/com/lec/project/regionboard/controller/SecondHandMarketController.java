package com.lec.project.regionboard.controller;

import com.lec.project.regionboard.model.*;
import com.lec.project.regionboard.service.SecondHandMarketService;
import com.lec.project.regionboard.repository.MemberRegionRepository;
import com.lec.project.regionboard.repository.SecondHandMarketRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import com.lec.project.Member;
import com.lec.project.MemberRepository;

@Controller
public class SecondHandMarketController {

    @Autowired
    private SecondHandMarketService marketService;
    
    @Autowired
    private SecondHandMarketRepository secondHandMarketRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberRegionRepository memberRegionRepository;

    // 중고마켓 접속 버튼 클릭 시 유저의 지역으로 자동 리다이렉트
    @GetMapping("/market/access")
    public String redirectToUserRegionMarket(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";  // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        }
        String memberId = userDetails.getUsername();

        // 로그인한 유저의 지역 정보 가져오기
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // 유저의 지역을 MemberRegion에서 찾아오기
        MemberRegion memberRegion = memberRegionRepository.findByMember(member);
        if (memberRegion == null) {
            // 유저의 지역 정보가 없다면, 에러 페이지로 리다이렉트
            return "redirect:/error"; // 또는 다른 오류 페이지로 이동
        }

        // 지역이 일치하면 해당 지역의 게시글 조회 페이지로 리다이렉트
        return "redirect:/market/region?region=" + memberRegion.getRegion();
    }    

    @GetMapping("/market/region")
    public String getPostsByRegion(@RequestParam(value = "region", required = false) String region,
                                    @RequestParam(value = "search", required = false) String search,
                                    @PageableDefault(size = 9) Pageable pageable,
                                    @AuthenticationPrincipal UserDetails userDetails,
                                    Model model) {
        
    	System.out.println("Region: " + region); // 로그로 지역 확인
        System.out.println("Search: " + search); // 로그로 검색어 확인
        
        if (region == null || region.isEmpty()) {
            // region 값이 없으면 기본 지역 값 설정
            region = "defaultRegion";  // 기본 지역 설정 (예: "서울")
        }

        String memberId = userDetails.getUsername();

        // 로그인한 유저의 지역 정보 가져오기
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // 유저의 지역을 MemberRegion에서 찾아오기
        MemberRegion memberRegion = memberRegionRepository.findByMember(member);
        if (memberRegion == null || !memberRegion.getRegion().equals(region)) {
            // 지역이 일치하지 않으면 오류 페이지로 이동
            return "redirect:/error"; // 또는 다른 오류 페이지로 리다이렉트
        }

        Page<SecondHandMarket> posts;
        if (search != null && !search.isEmpty()) {
            // 제목으로 검색
            posts = marketService.getPostsByRegionAndTitle(region, search, pageable);
        } else {
            // 지역별 게시글 조회
            posts = marketService.getPostsByRegion(region, pageable);
        }

        // region 값 인코딩
        String encodedRegion = URLEncoder.encode(region, StandardCharsets.UTF_8);

        // 모델에 게시글 리스트와 인코딩된 지역값 추가
        model.addAttribute("posts", posts);
        model.addAttribute("region", encodedRegion);  // 인코딩된 지역값 추가
        model.addAttribute("search", search);  // 검색어 추가

        return "SecondHandMarket/regionMarket";
    }



    // 게시글 작성 폼 이동
    @GetMapping("/market/write")
    public String showWriteForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String memberId = userDetails.getUsername();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        MemberRegion memberRegion = memberRegionRepository.findByMember(member);

        model.addAttribute("memberId", memberId);
        model.addAttribute("userRegion", memberRegion != null ? memberRegion.getRegion() : "지역 정보 없음");
        return "SecondHandMarket/writeMarket"; // writeMarket.html 반환
    }

    private static final String UPLOAD_DIR = "D:/sungil/98.temp/upload/";
    
    // 게시글 작성 처리
    @PostMapping("/market/write")
    public String savePost(@RequestParam("title") String title,
                           @RequestParam("content") String content,
                           @RequestParam("price") Double price,
                           @RequestParam("region") String region,
                           @RequestParam("thumbnail") MultipartFile thumbnail,
                           @RequestParam("contentImage") MultipartFile contentImage,
                           @AuthenticationPrincipal UserDetails userDetails) {

        String memberId = userDetails.getUsername();

        // 데이터베이스에서 Member 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // 썸네일 파일 저장
        String thumbnailFilename = saveFile(thumbnail);

        // 본문 이미지 파일 저장
        String contentImageFilename = saveFile(contentImage);

        SecondHandMarket post = SecondHandMarket.builder()
                .title(title)
                .content(content)
                .price(price)
                .region(region)
                .thumbnail(thumbnailFilename)  // 썸네일 파일 경로 저장
                .contentImage(contentImageFilename) // 본문 이미지 파일 경로 저장
                .member(member) // 데이터베이스에서 가져온 Member 사용
                .build();

        marketService.savePost(post);
        return "redirect:/market/region?region=" + region;
    }


    // 파일 저장 처리 (썸네일, 본문 이미지)
    private String saveFile(MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }

        // 파일 이름을 UUID로 설정하여 중복 방지
        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        File destinationFile = new File(UPLOAD_DIR + filename);

        try {
            file.transferTo(destinationFile);  // 파일을 서버에 저장
        } catch (IOException e) {
            e.printStackTrace();
            return null;  // 파일 저장 실패 시 null 반환
        }

        // 웹 경로로 반환
        return "uploads/" + filename;  // 파일 경로를 웹에서 접근할 수 있도록 설정
    }

    
    // 게시글 상세 조회
    @GetMapping("/market/detail/{postId}")
    public String viewPostDetail(@PathVariable("postId") Long postId,
                                @AuthenticationPrincipal UserDetails userDetails,
                                Model model) {

        SecondHandMarket post = marketService.getPostById(postId);

        if (post == null || post.getMember() == null) {
            // 오류 발생 시 직접 오류 페이지를 반환하거나, 예외 처리로 리다이렉트할 수 있습니다.
            return "error"; // 오류 페이지로 리다이렉트
        }
        // 로그인한 사용자 ID
        String userId = userDetails.getUsername();

        boolean isAuthor = post.getMember().getId().equals(userId);

        model.addAttribute("post", post);
        model.addAttribute("userId", userId);
        model.addAttribute("isAuthor", isAuthor);

        return "SecondHandMarket/marketDetail";
    }

    // 게시글 삭제
    @PostMapping("/market/delete/{postId}")
    public String deletePost(@PathVariable("postId") Long postId, 
                             @AuthenticationPrincipal UserDetails userDetails) {
        // 게시글 조회
        SecondHandMarket post = secondHandMarketRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다"));

        // 작성자 확인
        if (post.getMember().getId().equals(userDetails.getUsername())) {
            // 게시글 삭제
            secondHandMarketRepository.delete(post);
        } else {
            // 작성자가 아니면 에러 페이지로 리다이렉트
            return "redirect:/error";
        }

        // 삭제 후 해당 지역 게시글 목록으로 리다이렉트
        return "redirect:/market/region?region=" + post.getRegion();
    }

    // 게시글 수정 페이지로 이동
    @GetMapping("/market/edit/{postId}")
    public String editPost(@PathVariable("postId") Long postId, Model model) {
        SecondHandMarket post = secondHandMarketRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        model.addAttribute("post", post);
        return "SecondHandMarket/marketEdit"; // 수정 페이지로 이동
    }

    // 게시글 수정 처리
    @PostMapping("/market/edit/{postId}")
    public String updatePost(@PathVariable("postId") Long postId, 
                             @ModelAttribute SecondHandMarket updatedPost,
                             @AuthenticationPrincipal UserDetails userDetails) {
        SecondHandMarket post = secondHandMarketRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // 로그인한 사용자가 작성자와 일치하는지 확인
        if (post.getMember().getId().equals(userDetails.getUsername())) {
            post.setTitle(updatedPost.getTitle());
            post.setContent(updatedPost.getContent());
            post.setPrice(updatedPost.getPrice());
            post.setRegion(updatedPost.getRegion());
            secondHandMarketRepository.save(post); // 수정된 내용 저장
        } else {
            return "redirect:/error"; // 권한이 없는 경우 오류 페이지로 이동
        }

        return "redirect:/market/detail/" + postId; // 수정된 게시글 상세 페이지로 리다이렉트
    }
}
