
package com.lec.project.shoppingmall.service.magement;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lec.project.shoppingmall.domain.cart.order.Ordered;
import com.lec.project.shoppingmall.domain.payment.kakao.KakaoPayment;
import com.lec.project.shoppingmall.domain.payment.kakao.KakaoPaymentStatus;
import com.lec.project.shoppingmall.dto.magement.OrderManagementDTO;
import com.lec.project.shoppingmall.dto.magement.RefundRequestDTO;
import com.lec.project.shoppingmall.repository.KakaoPaymentRepository;
import com.lec.project.shoppingmall.repository.OrderedRepository;
import com.lec.project.shoppingmall.service.kakaopay.KakaoPaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class OrderManagementServiceImpl implements OrderManagementService {
	
	private final OrderedRepository orderedRepository;
	private final KakaoPaymentRepository kakaoPaymentRepository;
	private final KakaoPaymentService kakaoPaymentService;

	// 주문 목록 조회 (필터링 및 페이징)
	@Transactional(readOnly = true)
	public Page<OrderManagementDTO> getOrderList(
		String status,
		LocalDateTime startDate,
		LocalDateTime endDate,
		String search,
		Pageable pageable
	) {
	    if (status == null || status.isEmpty()) {
	        // 환불 요청 상태가 아닌 주문만 조회
	        return orderedRepository.searchOrders(
	            null, 
	            startDate, 
	            endDate, 
	            search, 
	            pageable
	        ).map(OrderManagementDTO::fromEntity);
	    }

	    return orderedRepository.searchOrders(status, startDate, endDate, search, pageable)
	            .map(OrderManagementDTO::fromEntity);
	}

	// 주문 상세 조회
	@Transactional(readOnly = true)
	public OrderManagementDTO getOrderDetails(Long orderId) {
		Ordered order = orderedRepository.findById(orderId)
				.orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

		return OrderManagementDTO.fromEntity(order);
	}
	
	// 주문 상태 변경
	public OrderManagementDTO updateOrderStatus(Long orderId, String status) {
		Ordered ordered = orderedRepository.findById(orderId)
				.orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
		
		//주문 상태 업데이트
		ordered.setStatus(status);
		
		return OrderManagementDTO.fromEntity(orderedRepository.save(ordered));
	}

	// 환불 요청 목록 조회
	@Transactional(readOnly = true)
	public List<RefundRequestDTO> getRefundRequests() {
		List<KakaoPayment> refundRequests = kakaoPaymentRepository.findByStatus(KakaoPaymentStatus.REFUND_REQUESTED);

		return refundRequests.stream()
				.map(payment -> RefundRequestDTO.builder()
						.orderId(payment.getOrder().getId())
						.customerId(payment.getPartnerUserId())
						.customerName(payment.getOrder().getMember().getName())
						.refundAmount(payment.getTotalAmount())
						.status(payment.getStatus())
						.requestDate(payment.getCreatedAt())
						.build())
				.collect(Collectors.toList());
	}

	// 환불 요청 처리
	public RefundRequestDTO processRefundRequest(
			Long orderId,
			boolean approve,
			String managerComment
	) {
		KakaoPayment kakaopayment = kakaoPaymentRepository.findByOrderId(orderId)
				.orElseThrow(() -> new IllegalArgumentException("결제 정보를 찾을 수 없습니다."));

		Ordered ordered = kakaopayment.getOrder();

		if (approve) {
			// 환불 승인 처리
			kakaopayment.setStatus(KakaoPaymentStatus.CANCELLED);
			ordered.setStatus("REFUND_SUCCESS");

			// 실제 환불 처리 로직 추가 (결제 시스템 API 호출 등) // 예: 카카오페이 환불 API 호출
//			kakaoPayService.refundPayment(kakaopayment.getTid(), kakaopayment.getTotalAmount());
		} else { // 환불 거절 처리
			kakaopayment.setStatus(KakaoPaymentStatus.APPROVED);
			ordered.setStatus("REFUND_REJECTED");
		}

		// 엔티티 저장
		kakaoPaymentRepository.save(kakaopayment);
		orderedRepository.save(ordered);

		return RefundRequestDTO.builder()
				.orderId(orderId)
				.status(kakaopayment.getStatus())
				.build();
	}

	// 주문 상태별 통계
	@Transactional(readOnly = true)
	public Map<String, Long> getOrderStatusStatistics() {
	    List<Ordered> orders = orderedRepository.findAll();

	    return orders.stream()
	        .filter(order -> order.getStatus() != null) // null 상태 제외
	        .collect(Collectors.groupingBy(
	            Ordered::getStatus, 
	            Collectors.counting()
	        ));
	}
}
