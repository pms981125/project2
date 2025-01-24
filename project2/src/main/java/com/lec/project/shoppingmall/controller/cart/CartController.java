package com.lec.project.shoppingmall.controller.cart;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lec.project.Member;
import com.lec.project.MemberRepository;
import com.lec.project.shoppingmall.dto.PageRequestDTO;
import com.lec.project.shoppingmall.dto.PageResponseDTO;
import com.lec.project.shoppingmall.dto.cart.CartListDTO;
import com.lec.project.shoppingmall.service.cart.CartService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
@Log4j2
public class CartController {

	private final CartService cartService;
	private final MemberRepository memberRepository;

	@GetMapping("/list")
	public void list(PageRequestDTO pageRequestDTO
			, @AuthenticationPrincipal UserDetails userDetails
			, Model model) {
		String memberId = userDetails.getUsername();
		model.addAttribute("memberId", memberId);
		
		PageResponseDTO<CartListDTO> responseDTO = cartService.list(pageRequestDTO, memberId);
		int totalPrice = cartService.getTotalPrice(memberId);

		model.addAttribute("responseDTO", responseDTO);
		model.addAttribute("totalPrice", totalPrice);
	}

	@GetMapping("/read")
	public void read(@RequestParam("id") Long id
			, @AuthenticationPrincipal UserDetails userDetails
			, Model model) {
		String memberId = userDetails.getUsername();
		CartListDTO dto = cartService.readOne(id, memberId);
		model.addAttribute("dto", dto);
	}

	@GetMapping("/modify")
	public void modify(@RequestParam("id") Long id
			, @AuthenticationPrincipal UserDetails userDetails
			, Model model) {
		String memberId = userDetails.getUsername();
		CartListDTO dto = cartService.readOne(id, memberId);
		model.addAttribute("dto", dto);
	}

	@PostMapping("/modify")
	public String modify(@RequestParam("id") Long id
			, @RequestParam("count") int count
			, @AuthenticationPrincipal UserDetails userDetails
			, RedirectAttributes redirectAttributes) {
		try {
			String memberId = userDetails.getUsername();
			cartService.modify(id, count, memberId);
			redirectAttributes.addFlashAttribute("result", "수정완료");
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/cart/modify?id=" + id;
		}
		return "redirect:/cart/read?id=" + id;
	}

	@PostMapping("/remove")
	public String remove(@RequestParam("id") Long id
			, @AuthenticationPrincipal UserDetails userDetails) {
	    String memberId = userDetails.getUsername();
	    cartService.remove(id, memberId);
	    return "redirect:/cart/list";
	}
	
	@GetMapping("/logout")
	public String logout() {
		return "redirect:/logout";
	}
	
}