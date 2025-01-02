package com.lec.project.shoppingmall.controller.shop;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lec.project.shoppingmall.domain.product.Product;
import com.lec.project.shoppingmall.dto.PageRequestDTO;
import com.lec.project.shoppingmall.dto.PageResponseDTO;
import com.lec.project.shoppingmall.dto.product.image.ProductImageDTO;
import com.lec.project.shoppingmall.dto.shop.ShopDTO;
import com.lec.project.shoppingmall.service.product.image.ProductImageService;
import com.lec.project.shoppingmall.service.shop.ShopService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Log4j2
@Controller
@RequestMapping("/protoshop")
@RequiredArgsConstructor
public class ShopController {

	private final ShopService shopService;
	private final ProductImageService productImageService;
	
	@GetMapping("/list")
	public String list(PageRequestDTO pageRequestDTO
			, @AuthenticationPrincipal UserDetails userDetails
			, @RequestParam(name = "keyword", required = false) String keyword
			, @RequestParam(name = "category", required = false) String category		
			, Model model) {
		
		log.info("category: " + category);
	    log.info("keyword: " + keyword);
		
		if(userDetails != null) {
			model.addAttribute("memberId", userDetails.getUsername());
		}
		
		PageResponseDTO<ShopDTO> responseDTO = shopService.list(pageRequestDTO, keyword, category);
		log.info(".........." + responseDTO);
		
		responseDTO.getDtoList().forEach(shopDTO -> {
			ProductImageDTO mainImage = productImageService.getMainImage(shopDTO.getProduct_code());
			if(mainImage != null) {
				shopDTO.setThumbnail_path(mainImage.getThumnail_path());
			}
		});
		
		model.addAttribute("responseDTO", responseDTO);
		model.addAttribute("keyword", keyword);
		model.addAttribute("category", category);
			
		return "protoshop/list";
	}
	
	@GetMapping({"/read", "/modify"})
	public void read(@RequestParam("bno") Long bno, Model model) {
			log.info("read or modify..........");
			ShopDTO shopDTO = shopService.readOne(bno);
			
			ProductImageDTO mainImage = productImageService.getMainImage(shopDTO.getProduct_code());
			if(mainImage != null) {
				shopDTO.setImg_path(mainImage.getImg_path());
				shopDTO.setThumbnail_path(mainImage.getThumnail_path());
			}
			model.addAttribute("dto", shopDTO);
			}
	
	@PreAuthorize("hasRole('MANAGER')")
	@GetMapping("/regist")
	public void registerGet() {
		log.info("regist.GET..........");
	}
	
	@PreAuthorize("hasRole('MANAGER')")
	@PostMapping("/regist")	// 상품등록
	public String registerPost(@Valid ShopDTO shopDTO	// 폼에서 전송된 데이터를 검증
			, @RequestParam(value = "productImage", required = false) MultipartFile productImage
			, BindingResult bindingResult				// 검증 결과를 담는 객체
			, RedirectAttributes redirectAttributes) {	// 리다이렉트시 데이터 전달
		log.info("regist.Post..........");
		
		if(bindingResult.hasErrors()) {
			log.info("입력된 정보에 에러가 있습니다...........");
			redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
			return "redirect:/protoshop/regist";
		}
		log.info("regist.........." + shopDTO);
		
		Long bno = shopService.register(shopDTO);
		
		if(productImage != null && !productImage.isEmpty()) {
			try {
				productImageService.uploadProductImage(productImage, shopDTO.getProduct_code(), true);
			} catch(Exception e) {
				log.error("이미지 업로드 실패..........", e);
				redirectAttributes.addFlashAttribute("imageError", "이미지 업로드에 실패했습니다.");
			}
		}
		redirectAttributes.addFlashAttribute("result", bno);
		
		return "redirect:/protoshop/list";
	}
	
	
	// 수정
	@PreAuthorize("hasRole('MANAGER')")
	@PostMapping("modify")
	public String modify(@Valid ShopDTO shopDTO
			, @RequestParam(value = "productImage", required = false) MultipartFile productImage
			, BindingResult bindingResult
			, RedirectAttributes redirectAttributes) {
		log.info("modify.Post : " + shopDTO);
		
		try {
			if(bindingResult.hasErrors()) {
				log.info("입력된 정보에 에러가 있습니다..........");
				
				redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
				redirectAttributes.addFlashAttribute("bno", shopDTO.getBno());
				
				return "redirect:/protoshop/modify?bno=" + shopDTO.getBno();
			}
			
			shopService.modify(shopDTO);
			
			// 이미지 업로드 (메인 이미지로)
            if (productImage != null && !productImage.isEmpty()) {
                try {
                    productImageService.uploadProductImage(productImage, shopDTO.getProduct_code(), true);
                } catch (Exception e) {
                    log.error("이미지 업로드 실패", e);
                    redirectAttributes.addFlashAttribute("imageError", "이미지 업로드에 실패했습니다.");
                }
            }			
			
			redirectAttributes.addFlashAttribute("result", "게시글수정성공..........");
			redirectAttributes.addFlashAttribute("bno", shopDTO.getBno());
			
			 return "redirect:/protoshop/read?bno=" + shopDTO.getBno();
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
	        return "redirect:/protoshop/modify?bno=" + shopDTO.getBno();
		}
	}
	
	@PreAuthorize("hasRole('MANAGER')")
	@PostMapping("remove")
	public String remove(@RequestParam("bno") Long bno, RedirectAttributes redirectAttributes) {
		log.info("remove.Post..........");
		
		shopService.remove(bno);
		redirectAttributes.addFlashAttribute("result", "게시글삭제성공..........");
		return "redirect:/protoshop/list";
	}
	
	@GetMapping("/logout")
	public String logout() {
		return "redirect:/logout";
	}
	
}

















