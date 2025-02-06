package com.lec.project.shoppingmall.controller.shop;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {

	private final ShopService shopService;
	private final ProductImageService productImageService;

	@GetMapping("/list")
	public String list(
			PageRequestDTO pageRequestDTO, 
			@AuthenticationPrincipal UserDetails userDetails,
			@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "category", required = false) String category,
			Model model) {

		log.info("category: " + category);
		log.info("keyword: " + keyword);

		if (userDetails != null) {
			model.addAttribute("memberId", userDetails.getUsername());
		}
		
		PageResponseDTO<ShopDTO> responseDTO = null;
		
		try {
			if(category != null && !category.isEmpty() && (keyword == null || keyword.isEmpty())) {
				//카테고리만 선택된경우 keyword를 null로..
				responseDTO = shopService.list(pageRequestDTO, null, category);
			} else {
				responseDTO = shopService.list(pageRequestDTO, keyword, category);
			}
			
			if(responseDTO != null && responseDTO.getDtoList() != null) {
				responseDTO.getDtoList().forEach(shopDTO -> {
					ProductImageDTO mainImage = productImageService.getMainImage(shopDTO.getProductCode());
					if(mainImage != null) {
						shopDTO.setImgPath(mainImage.getImgPath());
						shopDTO.setThumbnailPath(mainImage.getThumbnailPath());
					}
				});
			}
		} catch (Exception e) {
			log.error("검색처리 중 오류 발생.....", e);
			model.addAttribute("searchError", "검색처리 중 오류가 발생헀습니다. 다시 시도해주세요");
		} finally {
			// responseDTO가 null이어도 빈 데이터로 처리
			if(responseDTO == null) {
				responseDTO = PageResponseDTO.<ShopDTO>withAll()
						.pageRequestDTO(pageRequestDTO)
						.dtoList(new ArrayList<>())
						.total(0)
						.build();
			}
		}

		model.addAttribute("responseDTO", responseDTO);
		model.addAttribute("keyword", keyword);
		model.addAttribute("category", category);
		return "shop/list";
	}

	@GetMapping({ "/read", "/modify" })
	public void read(
			@RequestParam(value = "bno", required = false) Long bno,
			@RequestParam(value = "productCode", required = false) String productCode,
			Model model) {
		log.info("read or modify..........");
		ShopDTO shopDTO;
		
		if(bno != null) {
			shopDTO = shopService.readOne(bno);
		} else if (productCode != null) {
			shopDTO = shopService.readByProductCode(productCode);
		} else {
			throw new IllegalArgumentException("bno 또는 productCode가 필요합니다.");
		}

		// 이미지 경로 로깅
		log.info("Shop DTO - Thumbnail Path: {}", shopDTO.getThumbnailPath());

		// 제품의 모든 이미지 가져오기
		List<ProductImageDTO> productImages = productImageService.getProductImages(shopDTO.getProductCode());

		ProductImageDTO mainImage = productImageService.getMainImage(shopDTO.getProductCode());
		if (mainImage != null) {
			shopDTO.setImgPath(mainImage.getImgPath());
			shopDTO.setThumbnailPath(mainImage.getThumbnailPath());
		}
		model.addAttribute("dto", shopDTO);
		model.addAttribute("productImages", productImages);
	}

	@PreAuthorize("hasRole('MANAGER')")
	@GetMapping("/regist")
	public void registerGet() {
		log.info("regist.GET..........");
	}

	@PreAuthorize("hasRole('MANAGER')")
	@PostMapping("/regist")
	public String registerPost(@Valid ShopDTO shopDTO,
	        BindingResult bindingResult,
	        @RequestParam(value = "productImages", required = false) List<MultipartFile> productImages,
	        RedirectAttributes redirectAttributes) {
	    log.info("regist.Post..........");
	    
	    try {
	        if (bindingResult.hasErrors()) {
	            log.info("입력된 정보에 에러가 있습니다...........");
	            redirectAttributes.addFlashAttribute("error", "필수 정보가 누락되었습니다. 상품을 먼저 조회해주세요.");
	            return "redirect:/shop/regist";
	        }

	        Long bno = shopService.register(shopDTO);

	        // 이미지가 제공된 경우에만 이미지 처리 로직 실행
	        if (productImages != null && !productImages.isEmpty() && !productImages.get(0).isEmpty()) {
	            try {
	                for (MultipartFile image : productImages) {
	                    String originalFilename = image.getOriginalFilename();
	                    if(originalFilename != null && !originalFilename.toLowerCase().endsWith(".jpg") 
	                       && !originalFilename.toLowerCase().endsWith(".jpeg")
	                       && !originalFilename.toLowerCase().endsWith(".png")
	                       && !originalFilename.toLowerCase().endsWith(".gif")) {
	                        redirectAttributes.addFlashAttribute("error", "이미지 파일만 업로드 가능합니다.");
	                        return "redirect:/shop/regist";
	                    }
	                }

	                productImageService.uploadMultipleProductImages(productImages, shopDTO.getProductCode(), true);
	            } catch (Exception e) {
	                log.error("이미지 업로드 실패..........", e);
	                redirectAttributes.addFlashAttribute("error", "이미지 업로드에 실패했습니다.");
	            }
	        }
	        redirectAttributes.addFlashAttribute("result", bno);
	    } catch(IllegalArgumentException e) {
	        log.error("상품 등록 실패..........", e);
	        redirectAttributes.addFlashAttribute("error", e.getMessage());
	        return "redirect:/shop/regist";
	    } catch (Exception e) {
	        log.error("등록 중 오류 발생..........", e);
	        redirectAttributes.addFlashAttribute("error", "등록 중 오류가 발생했습니다.");
	        return "redirect:/shop/regist";
	    }

	    return "redirect:/shop/list";
	}

	// 수정
	@PreAuthorize("hasRole('MANAGER')")
	@PostMapping("modify")
	public String modify(@Valid ShopDTO shopDTO,
			@RequestParam(value = "productImages", required = false) List<MultipartFile> productImages,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		log.info("modify.Post : " + shopDTO);

		try {
			if (bindingResult.hasErrors()) {
				log.info("입력된 정보에 에러가 있습니다..........");

				redirectAttributes.addFlashAttribute("error", bindingResult.getAllErrors());
				redirectAttributes.addFlashAttribute("bno", shopDTO.getBno());

				return "redirect:/shop/modify?bno=" + shopDTO.getBno();
			}

			shopService.modify(shopDTO);

			// 이미지 업로드 (메인 이미지로)
			if (productImages != null && !productImages.isEmpty()) {
				try {
					for (int i = 0; i < productImages.size(); i++) {
						MultipartFile productImage = productImages.get(i);
						boolean isMainImage = (i == 0);

						ProductImageDTO imageDTO = productImageService.uploadProductImage(productImage,
								shopDTO.getProductCode(), isMainImage);

						if (imageDTO != null) {
							log.info("Image uploaded successfully: {}", imageDTO.getImgPath());
						}
					}
				} catch (Exception e) {
					log.error("이미지 업로드 실패", e);
					redirectAttributes.addFlashAttribute("imageError", "이미지 업로드에 실패했습니다.");
				}
			}

			redirectAttributes.addFlashAttribute("result", "게시글수정성공..........");
			redirectAttributes.addFlashAttribute("bno", shopDTO.getBno());

			return "redirect:/shop/read?bno=" + shopDTO.getBno();
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/shop/modify?bno=" + shopDTO.getBno();
		}
	}

	@PreAuthorize("hasRole('MANAGER')")
	@PostMapping("remove")
	public String remove(@RequestParam("bno") Long bno,
			RedirectAttributes redirectAttributes) {
	    log.info("remove.Post..........");

	    try {
	        // 게시글 정보를 먼저 가져옴
	        ShopDTO shopDTO = shopService.readOne(bno);
	        if (shopDTO != null) {
	            // 해당 상품의 모든 이미지를 삭제
	            List<ProductImageDTO> images = productImageService.getProductImages(shopDTO.getProductCode());
	            if (images != null && !images.isEmpty()) {
	                for (ProductImageDTO image : images) {
	                    productImageService.deleteImage(image.getImgId());
	                }
	            }
	        }

	        // 게시글 삭제
	        shopService.remove(bno);
	        redirectAttributes.addFlashAttribute("result", "게시글삭제성공..........");
	        
	    } catch (Exception e) {
	        log.error("게시글 삭제 중 오류 발생", e);
	        redirectAttributes.addFlashAttribute("error", "게시글 삭제 중 오류가 발생했습니다.");
	    }
	    
	    return "redirect:/shop/list";
	}
	
	@PreAuthorize("hasRole('MANAGER')")
	@PostMapping("/deleteImage")
	public ResponseEntity<?>deleteImage(
			@RequestParam("imageId") String imageId,
			@RequestParam("productCode") String productCode
			){
		try {
			productImageService.deleteImage(imageId);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			log.error("이미지 삭제 실패.........", e);
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/logout")
	public String logout() {
		return "redirect:/logout";
	}

}
