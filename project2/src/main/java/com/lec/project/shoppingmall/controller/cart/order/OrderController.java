package com.lec.project.shoppingmall.controller.cart.order;

import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lec.project.Member;
import com.lec.project.MemberRepository;
import com.lec.project.account.domain.Account;
import com.lec.project.account.repository.AccountRepository;
import com.lec.project.shoppingmall.dto.PageRequestDTO;
import com.lec.project.shoppingmall.dto.PageResponseDTO;
import com.lec.project.shoppingmall.dto.cart.CartListDTO;
import com.lec.project.shoppingmall.dto.cart.order.OrderSubmitDTO;
import com.lec.project.shoppingmall.service.cart.CartService;
import com.lec.project.shoppingmall.service.cart.order.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
@Log4j2
public class OrderController {

	private final OrderService orderService;
	private final MemberRepository memberRepository;
	private final AccountRepository accountRepository;
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
	        
			// 회원의 계좌 정보 조회 - 첫 번째 계좌 가져오기
			Page<Account> accountPage = accountRepository.findByMemberId(memberId, pageRequestDTO.getPageable("accountId"));
			if(accountPage.isEmpty()) {
				//계좌가 없는 경우 account/list로 redirect
				redirectAttributes.addFlashAttribute("error", "결제를 위한 계좌가 필요합니다. 계좌를 먼저 등록해주세요.");
				return "redirect:/account/list";
			}
	        
			Account account = accountPage.getContent().get(0);
	        
			// manager의 계좌 정보 조회
			Page<Account> managerAccountPage = accountRepository.findByMemberId("manager", pageRequestDTO.getPageable("accountId"));
			if(managerAccountPage.isEmpty()) {
				// 관리자 계좌가 없는 경우 cart/list로 redirect
				redirectAttributes.addFlashAttribute("error", "현재 결제 시스템에 문제가 있습니다. 관리자에게 문의해주세요.");
				return "redirect:/cart/list";
				}
	        
			Account managerAccount = managerAccountPage.getContent().get(0);
	        
			model.addAttribute("member", member);
			model.addAttribute("account", account);
			model.addAttribute("managerAccount", managerAccount);
	        
			PageResponseDTO<CartListDTO> responseDTO = cartService.list(pageRequestDTO, memberId);
			int totalPrice = cartService.getTotalPrice(memberId);
	
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
			RedirectAttributes redirectAttributes) {
		try {
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
}
