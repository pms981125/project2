package com.lec.project.regionboard.service;

import com.lec.project.regionboard.model.SecondHandMarket;
import com.lec.project.regionboard.repository.SecondHandMarketRepository;
import com.lec.project.Member;
import com.lec.project.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SecondHandMarketService {

    @Autowired
    private SecondHandMarketRepository marketRepository;

    @Autowired
    private MemberRepository memberRepository; // MemberRepository 주입

    // 게시글 저장
    public void savePost(SecondHandMarket post) {
        // 데이터베이스에서 Member 조회
        Member member = memberRepository.findById(post.getMember().getId())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // 조회된 Member 객체를 SecondHandMarket에 설정
        post.setMember(member);

        // 게시글 저장
        marketRepository.save(post);
    }
    
    // 지역별 게시글 조회
    public Page<SecondHandMarket> getPostsByRegion(String region, Pageable pageable) {
        return marketRepository.findByRegion(region, pageable);
    }
    
    public Page<SecondHandMarket> getPostsByRegionAndTitle(String region, String search, Pageable pageable) {
        return marketRepository.findByRegionAndTitleContaining(region, search, pageable);
    }

    // 게시글 상세 조회 메서드
    public SecondHandMarket getPostById(Long postId) {
        Optional<SecondHandMarket> post = marketRepository.findById(postId);
        return post.orElseThrow(() -> new RuntimeException("Post not found"));
    }
    


}
