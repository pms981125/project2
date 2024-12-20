package com.lec.project.shoppingmall.controller.shop;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lec.project.shoppingmall.domain.product.Product;
import com.lec.project.shoppingmall.dto.PageRequestDTO;
import com.lec.project.shoppingmall.dto.PageResponseDTO;
import com.lec.project.shoppingmall.dto.shop.ShopDTO;
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
	
	@GetMapping("/list")
	public String glist(PageRequestDTO pageRequestDTO, Model model) {
		PageResponseDTO<ShopDTO> responseDTO = shopService.list(pageRequestDTO);
		log.info(".........." + responseDTO);
		model.addAttribute("responseDTO", responseDTO);
		return "protoshop/list";
	}
	
	@GetMapping({"/read", "/modify"})
	public void read(@RequestParam("bno") Long bno
			, PageRequestDTO pageRequestDTO, Model model) {
			log.info("read or modify..........");
			ShopDTO shopDTO = shopService.readOne(bno);
			model.addAttribute("dto", shopDTO);
			}
	
	@GetMapping("/register")
	public void registerGet() {
		log.info("register.GET..........");
	}
	
	@PostMapping("/register")	// 상품등록
	public String registerPost(@Valid ShopDTO shopDTO	// 폼에서 전송된 데이터를 검증
			, BindingResult bindingResult				// 검증 결과를 담는 객체
			, RedirectAttributes redirectAttributes) {	// 리다이렉트시 데이터 전달
		log.info("register.Post..........");
		
		if(bindingResult.hasErrors()) {
			log.info("입력된 정보에 에러가 있습니다...........");
			redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
			return "redirect:/protoshop/register";
		}
		log.info("register.........." + shopDTO);
		
		Long bno = shopService.register(shopDTO);
		redirectAttributes.addFlashAttribute("result", bno);
		
		return "redirect:/protoshop/list";
	}
	
	@PostMapping("modify")
	public String modify(PageRequestDTO pageRequestDTO
			, @Valid ShopDTO shopDTO
			, BindingResult bindingResult
			, RedirectAttributes redirectAttributes) {
		log.info("modify.Post : " + shopDTO);
		
		if(bindingResult.hasErrors()) {
			log.info("입력된 정보에 에러가 있습니다..........");
			
			String link = pageRequestDTO.getLink();
			redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
			redirectAttributes.addFlashAttribute("bno", shopDTO.getBno());
			
			return "redirect:/protoshop/modify?" + link;
		}
		
		shopService.modify(shopDTO);
		redirectAttributes.addFlashAttribute("result", "게시글수정성공..........");
		redirectAttributes.addFlashAttribute("bno", shopDTO.getBno());
		
		return "redirect:/protoshop/read";
	}
	
	
	@PostMapping("remove")
	public String remove(@RequestParam("bno") Long bno, RedirectAttributes redirectAttributes) {
		log.info("remove.Post..........");
		
		shopService.remove(bno);
		redirectAttributes.addFlashAttribute("result", "게시글삭제성공..........");
		return "redirect:/protoshop/list";
	}
	
}

















