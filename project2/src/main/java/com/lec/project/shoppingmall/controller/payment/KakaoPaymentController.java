package com.lec.project.shoppingmall.controller.payment;

import java.util.Arrays;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lec.project.shoppingmall.domain.payment.kakao.KakaoPayment;
import com.lec.project.shoppingmall.dto.cart.order.OrderSubmitDTO;
import com.lec.project.shoppingmall.dto.payment.kakao.KakaoPayApproveRequest;
import com.lec.project.shoppingmall.repository.KakaoPaymentRepository;
import com.lec.project.shoppingmall.service.kakaopay.KakaoPaymentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/cart/order/kakao")
@RequiredArgsConstructor
@Log4j2
public class KakaoPaymentController {
    
    private final KakaoPaymentService kakaoPaymentService;
    private final KakaoPaymentRepository kakaoPaymentRepository;
    
    @GetMapping("/success")
    public String paymentSuccess(
    		HttpServletRequest httpServletRequest,
            @RequestParam("pg_token") String pgToken,
            @RequestParam(value = "partner_order_id", required = false) String partnerOrderId,
            @RequestParam(value = "tid", required = false) String tid,
            @AuthenticationPrincipal UserDetails userDetails,
            @SessionAttribute("orderSubmitDTO") OrderSubmitDTO orderSubmitDTO,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
    	
    	log.info("OrderSubmitDTO from session: {}", orderSubmitDTO);
        log.info("Payment success callback - All parameters:");
        log.info("pg_token: {}", pgToken);
        log.info("tid: {}", tid);
        log.info("partner_order_id: {}", partnerOrderId);
    	httpServletRequest.getParameterMap().forEach((key, value) -> {
		    log.info("{}={}", key, Arrays.toString(value));
		});
    	
        try {
            if(orderSubmitDTO == null) {
                orderSubmitDTO = (OrderSubmitDTO) session.getAttribute("orderSubmitDTO");
            }
            
			 // partner_order_id로 결제 정보 조회
			KakaoPayment kakaoPayment = kakaoPaymentRepository.findByPartnerUserIdOrderByCreatedAtDesc(userDetails.getUsername())
				.stream()
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("결제 정보를 찾을 수 없습니다."));

            KakaoPayApproveRequest request = KakaoPayApproveRequest.builder()
                .pgToken(pgToken)
                .partnerOrderId(kakaoPayment.getPartnerOrderId())
                .tid(kakaoPayment.getTid())
                .partnerUserId(userDetails.getUsername())
                .build();
                
            kakaoPaymentService.approvePayment(request, orderSubmitDTO);
            session.removeAttribute("orderSubmitDTO");
            
            redirectAttributes.addFlashAttribute("message", "결제가 완료되었습니다.");
            return "redirect:/shop/list";
            
        } catch (Exception e) {
        	 log.error("Payment approval failed", e);
            redirectAttributes.addFlashAttribute("error", "결제 처리 중 오류가 발생했습니다.");
            return "redirect:/cart/list";
        }
    }
    
    @GetMapping("/cancel")
    public String paymentCancel(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "결제가 취소되었습니다.");
        return "redirect:/cart/list";
    }
    
    @GetMapping("/fail")
    public String paymentFail(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "결제에 실패했습니다.");
        return "redirect:/cart/list";
    }
}
