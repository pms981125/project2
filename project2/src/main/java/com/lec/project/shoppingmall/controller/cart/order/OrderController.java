package com.lec.project.shoppingmall.controller.cart.order;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lec.project.Member;
import com.lec.project.MemberRepository;
import com.lec.project.shoppingmall.dto.PageRequestDTO;
import com.lec.project.shoppingmall.dto.PageResponseDTO;
import com.lec.project.shoppingmall.dto.cart.CartListDTO;
import com.lec.project.shoppingmall.dto.cart.order.OrderSubmitDTO;
import com.lec.project.shoppingmall.service.cart.CartService;
import com.lec.project.shoppingmall.service.cart.order.OrderService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
@Log4j2
public class OrderController {

	private final OrderService orderService;
	private final MemberRepository memberRepository;
	private final CartService cartService;
	
	@GetMapping("/order")
	public String order(PageRequestDTO pageRequestDTO
			, @AuthenticationPrincipal UserDetails userDetails
			, Model model
			, RedirectAttributes redirectAttributes) {
		
		String memberId = userDetails.getUsername();
        try {
			Member member = memberRepository.findById(memberId)
					.orElseThrow(() -> new RuntimeException("Member not found"));
	        
			model.addAttribute("member", member);
	        
			PageResponseDTO<CartListDTO> responseDTO = cartService.list(pageRequestDTO, memberId);
			int totalPrice = cartService.getTotalPrice(memberId);
			
			// totalCount 계산
			int totalCount = responseDTO.getDtoList().stream()
								.mapToInt(CartListDTO::getCount)
								.sum();
			model.addAttribute("totalCount", totalCount);
	
			// 장바구니가 비어있는 경우
			if (responseDTO.getDtoList().isEmpty()) {
				redirectAttributes.addFlashAttribute("error", "장바구니가 비어있습니다.");
				return "redirect:/cart/list";
				}
	
			model.addAttribute("responseDTO", responseDTO);
			model.addAttribute("totalPrice", totalPrice);
	        
			return "cart/order";
			
	    } catch (Exception e) {
	        log.error("주문 페이지 로딩 중 오류 발생", e);
	        redirectAttributes.addFlashAttribute("error", "주문 처리 중 오류가 발생했습니다. 다시 시도해주세요.");
	        return "redirect:/cart/list";
	   }
}
	
	@PostMapping("/order/submit")
	public String submitOrder(
			@AuthenticationPrincipal UserDetails userDetails,
			OrderSubmitDTO orderSubmitDTO,
			HttpSession session,
			RedirectAttributes redirectAttributes
	) {
		try {
			
			session.setAttribute("orderSubmitDTO", orderSubmitDTO);
			// OrderService를 통한 주문 처리
			orderService.createOrder(userDetails.getUsername(), orderSubmitDTO);
			 
			redirectAttributes.addFlashAttribute("message", "주문이 완료되었습니다.");
			return "redirect:/shop/list";
			 
		} catch (Exception e) {
			log.error("주문 처리 중 오류 발생", e);
			redirectAttributes.addFlashAttribute("error", "주문 처리 중 오류가 발생했습니다.");
			return "redirect:/cart/order";
		}
	}
	@PostMapping("/order/saveOrderInfo")
	@ResponseBody
	public ResponseEntity<String> saveOrderInfo(
	    @RequestBody OrderSubmitDTO orderSubmitDTO,
	    HttpSession session
	) {
	    session.setAttribute("orderSubmitDTO", orderSubmitDTO);
	    return ResponseEntity.ok("success");
	}
}
