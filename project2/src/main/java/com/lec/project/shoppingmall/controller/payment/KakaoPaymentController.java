package com.lec.project.shoppingmall.controller.payment;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lec.project.shoppingmall.dto.payment.kakao.KakaoPayApproveRequest;
import com.lec.project.shoppingmall.service.kakaopay.KakaoPaymentService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/cart/order/kakao")
@RequiredArgsConstructor
public class KakaoPaymentController {
    
    private final KakaoPaymentService kakaoPaymentService;
    
    @GetMapping("/success")
    public String paymentSuccess(
            @RequestParam("pg_token") String pgToken,
            @RequestParam("partner_order_id") String partnerOrderId,
            @RequestParam("tid") String tid,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        
        try {
            KakaoPayApproveRequest request = KakaoPayApproveRequest.builder()
                .pgToken(pgToken)
                .partnerOrderId(partnerOrderId)
                .tid(tid)
                .partnerUserId(userDetails.getUsername())
                .build();
                
            kakaoPaymentService.approvePayment(request);
            redirectAttributes.addFlashAttribute("message", "결제가 완료되었습니다.");
            return "redirect:/shop/list";
        } catch (Exception e) {
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
