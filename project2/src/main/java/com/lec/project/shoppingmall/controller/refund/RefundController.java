package com.lec.project.shoppingmall.controller.refund;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lec.project.shoppingmall.domain.cart.order.OrderStatus;
import com.lec.project.shoppingmall.dto.magement.OrderManagementDTO;
import com.lec.project.shoppingmall.service.magement.OrderManagementService;
import com.lec.project.shoppingmall.service.refund.RefundService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/refunds")
@RequiredArgsConstructor
@Log4j2
public class RefundController {

    private final RefundService refundService;
    private final OrderManagementService orderManagementService;

    // 회원의 환불 목록 페이지
    @GetMapping("/my")
    public String myRefundList(
        @AuthenticationPrincipal UserDetails userDetails,
        @PageableDefault(size = 10) Pageable pageable,
        Model model
    ) {
        model.addAttribute("refunds", 
            refundService.getMemberRefunds(userDetails.getUsername(), pageable));
        return "refund/myRefundList";
    }

    // 관리자용 환불 관리 페이지
    @GetMapping("/management")
    public String refundManagement(
        @RequestParam(name = "status", required = false) String status,
        @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
        @RequestParam(name = "search", required = false) String search,
        @PageableDefault(size = 10) Pageable pageable,
        Model model
    ) {
    	Page<OrderManagementDTO> refundRequests;
    	
    	if (status != null && !status.isEmpty()) {
	        refundRequests = orderManagementService.getOrderList(
	        	status,
	            startDate,
	            endDate,
	            search,
	            pageable
        );
    	} else {
            // 환불요청 상태의 주문만 조회
            refundRequests = orderManagementService.getOrderList(
                "REFUND_REQUESTED",
                startDate,
                endDate,
                search,
                pageable
            );
        }
    	
        model.addAttribute("refundList", refundRequests);
        
        // 환불 상태 목록 추가
        model.addAttribute("statusList", OrderStatus.values());
        
        return "management/refundManagement";
    }
    
    // 환불 상세 페이지
    @GetMapping("/{refundId}")
    public String refundDetail(
        @PathVariable Long refundId,
        Model model
    ) {
        model.addAttribute("refund", refundService.getRefundDetails(refundId));
        return "refund/refundDetail";
    }
}
