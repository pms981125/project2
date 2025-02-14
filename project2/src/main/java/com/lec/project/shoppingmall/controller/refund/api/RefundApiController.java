package com.lec.project.shoppingmall.controller.refund.api;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lec.project.shoppingmall.domain.cart.order.OrderStatus;
import com.lec.project.shoppingmall.dto.refund.UserRefundListResponseDTO;
import com.lec.project.shoppingmall.dto.refund.UserRefundRequestDTO;
import com.lec.project.shoppingmall.dto.refund.UserRefundDetailResponseDTO;
import com.lec.project.shoppingmall.dto.refund.UserRefundStatusUpdateDTO;
import com.lec.project.shoppingmall.service.refund.RefundService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api/refunds")
@RequiredArgsConstructor
@Log4j2
public class RefundApiController {

	private final RefundService refundService;
    
    // 환불 요청 생성
    @PostMapping
    public ResponseEntity<UserRefundDetailResponseDTO> createRefund(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody UserRefundRequestDTO refundRequestDTO
    ) {
        return ResponseEntity.ok(
            refundService.createRefund(userDetails.getUsername(), refundRequestDTO)
        );
    }
    
    // 환불 상태 업데이트 (관리자용)
    @PatchMapping("/{refundId}/status")
    public ResponseEntity<UserRefundDetailResponseDTO> updateRefundStatus(
        @PathVariable("refundId") Long refundId,
        @RequestBody OrderStatus newStatus
    ) {
        UserRefundStatusUpdateDTO updateDTO = UserRefundStatusUpdateDTO.builder()
            .refundId(refundId)
            .newStatus(newStatus)
            .build();
            
        return ResponseEntity.ok(refundService.updateRefundStatus(updateDTO));
    }
    
    // 환불 상세 조회
    @GetMapping("/{refundId}")
    public ResponseEntity<UserRefundDetailResponseDTO> getRefundDetails(
        @PathVariable("refundId") Long refundId
    ) {
        try {
            UserRefundDetailResponseDTO dto = refundService.getRefundDetails(refundId);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            // 잘못된 요청이나 데이터를 찾을 수 없는 경우
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            log.error("환불 상세 정보 조회 중 오류 발생", e);
            return ResponseEntity.internalServerError().body(null);
        }
    }
    
    // 환불 목록 조회 (관리자용)
    @GetMapping("/management")
    public ResponseEntity<Page<UserRefundListResponseDTO>> getRefundList(
        @RequestParam(name = "status", required = false) String status,
        @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
        @RequestParam(name = "search", required = false) String search,
        @PageableDefault(size = 10) Pageable pageable
    ) {
        log.info("환불 관리 API 요청 파라미터:");
        log.info("Status: {}", status);
        log.info("Start Date: {}", startDate);
        log.info("End Date: {}", endDate);
        log.info("Search: {}", search);
        log.info("Pageable: {}", pageable);
    	
    	OrderStatus orderStatus = null;
        if (status != null && !status.isEmpty()) {
            try {
                orderStatus = OrderStatus.valueOf(status);
            } catch (IllegalArgumentException e) {
                // 잘못된 상태값이 들어왔을 때 처리
                return ResponseEntity.badRequest().build();
            }
        }
    	
		return ResponseEntity.ok(
		        refundService.getRefundList(orderStatus, startDate, endDate, search, pageable)
		);
    }
    
    // 회원별 환불 목록 조회
    @GetMapping("/my")
    public ResponseEntity<Page<UserRefundListResponseDTO>> getMyRefunds(
        @AuthenticationPrincipal UserDetails userDetails,
        @PageableDefault(size = 10) Pageable pageable
    ) {
        return ResponseEntity.ok(
            refundService.getMemberRefunds(userDetails.getUsername(), pageable)
        );
    }

}
