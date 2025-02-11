package com.lec.project.shoppingmall.controller.management.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lec.project.shoppingmall.dto.magement.OrderManagementDTO;
import com.lec.project.shoppingmall.dto.magement.RefundProcessRequestDTO;
import com.lec.project.shoppingmall.dto.magement.RefundRequestDTO;
import com.lec.project.shoppingmall.service.cart.order.OrderService;
import com.lec.project.shoppingmall.service.magement.OrderManagementService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api/manager/orders")
@RequiredArgsConstructor
@Log4j2
public class OrderManagementRestController {

	private final OrderManagementService orderManagementService;
	private final OrderService orderService;
	
	@GetMapping("/{orderId}")
	public ResponseEntity<OrderManagementDTO> getOrderDetails(@PathVariable("orderId") Long orderId) {
	    OrderManagementDTO orderDetails = orderManagementService.getOrderDetails(orderId);
	    return ResponseEntity.ok(orderDetails);
	}
	
	// 주문상태 변경
	//@PatchMapping: HTTP PATCH 매서드 처리, 리소스의 일부(특정필드)만 수정할 떄 사용
	@PatchMapping("/{orderId}/status")
	public ResponseEntity<OrderManagementDTO> updateOrderStatus(
		@PathVariable("orderId") Long orderId,
		@RequestBody Map<String, String> payload
	) {
		String status = payload.get("status");
		OrderManagementDTO updateOrder = orderManagementService.updateOrderStatus(orderId, status);
		return ResponseEntity.ok(updateOrder);
	}
	
    // 환불 요청 목록 조회
    @GetMapping("/refunds")
    public ResponseEntity<List<RefundRequestDTO>> getRefundRequests() {
        List<RefundRequestDTO> refundRequests = orderManagementService.getRefundRequests();
        return ResponseEntity.ok(refundRequests);
    }
    
    // 환불 요청 처리
    @PostMapping("/refunds/{orderId}/process")
    public ResponseEntity<RefundRequestDTO> processRefundRequest(
        @PathVariable("orderId") Long orderId,
        @RequestBody RefundProcessRequestDTO RefundProcessRequest
    ) {
        RefundRequestDTO processedRefund = orderManagementService.processRefundRequest(
            orderId, 
            RefundProcessRequest.isApprove(), 
            RefundProcessRequest.getManagerComment()
        );
        return ResponseEntity.ok(processedRefund);
    }
    
    @PostMapping("/{orderId}/refund")
    @ResponseBody
    public ResponseEntity<String> requestRefund(@PathVariable("orderId") Long orderId) {
    	try {
            orderService.requestRefund(orderId);
            return ResponseEntity.ok("환불이 요청되었습니다.");
    	} catch (Exception e) {
    		return ResponseEntity.badRequest().body("환불 요청 처리 중 오류가 발생했습니다.");
    	}
    }
    
    @GetMapping("/management")  
    public ResponseEntity<Page<OrderManagementDTO>> filterOrders(
		@RequestParam(name = "status", required = false) String status,
		@RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
		@RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
		@RequestParam(name = "search", required = false) String search, 
		@PageableDefault(size = 10) Pageable pageable
    ) {
        Page<OrderManagementDTO> filteredOrders = orderManagementService.getOrderList(
            status, startDate, endDate, search, pageable
        );
        return ResponseEntity.ok(filteredOrders);
    }
}
