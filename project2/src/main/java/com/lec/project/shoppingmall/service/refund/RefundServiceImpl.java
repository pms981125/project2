package com.lec.project.shoppingmall.service.refund;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lec.project.shoppingmall.domain.cart.order.OrderStatus;
import com.lec.project.shoppingmall.domain.cart.order.Ordered;
import com.lec.project.shoppingmall.domain.payment.kakao.KakaoPayment;
import com.lec.project.shoppingmall.domain.refund.Refund;
import com.lec.project.shoppingmall.dto.payment.kakao.KakaoPayRefundRequestDTO;
import com.lec.project.shoppingmall.dto.refund.UserRefundDetailResponseDTO;
import com.lec.project.shoppingmall.dto.refund.UserRefundListResponseDTO;
import com.lec.project.shoppingmall.dto.refund.UserRefundRequestDTO;
import com.lec.project.shoppingmall.dto.refund.UserRefundStatusUpdateDTO;
import com.lec.project.shoppingmall.repository.KakaoPaymentRepository;
import com.lec.project.shoppingmall.repository.OrderedRepository;
import com.lec.project.shoppingmall.repository.RefundRepository;
import com.lec.project.shoppingmall.service.kakaopay.KakaoPaymentService;
import com.lec.project.shoppingmall.service.magement.OrderManagementService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class RefundServiceImpl implements RefundService {

	private final RefundRepository refundRepository;
	private final OrderedRepository orderedRepository;
	private final KakaoPaymentRepository kakaoPaymentRepository;
	private final KakaoPaymentService kakaoPaymentService;
	private final OrderManagementService orderManagementService;
	
	@Override
	public UserRefundDetailResponseDTO createRefund(String memberId, UserRefundRequestDTO refundRequestDTO) {
		
		// 주문 조회
        Ordered ordered = orderedRepository.findById(refundRequestDTO.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
		
        // 본인 확인
        if (!ordered.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("본인의 주문만 환불 신청이 가능합니다.");
        }
        
        // 환불 상태 확인
        if (refundRepository.findByOrderId(ordered.getId()).isPresent()) {
            throw new IllegalStateException("이미 환불이 신청된 주문입니다.");
        }
        
        // 환불 가능한 상태 제한
        OrderStatus currentStatus = OrderStatus.valueOf(ordered.getStatus());
        if (!Arrays.asList(OrderStatus.PENDING, OrderStatus.APPROVED).contains(currentStatus)) {
            throw new IllegalStateException("현재 상태에서는 환불할 수 없습니다.");
        }
        
        Refund refund = Refund.builder()
                .order(ordered)
                .customerName(ordered.getCustomerName())
                .member(ordered.getMember())
                .previousStatus(currentStatus)
                .refundReason(refundRequestDTO.getRefundReason())
                .build();
        
        // 주문 상태 업데이트
        ordered.setStatus(OrderStatus.REFUND_REQUESTED.name());
        orderedRepository.save(ordered);

        Refund savedRefund = refundRepository.save(refund);
        
        return UserRefundDetailResponseDTO.fromEntity(refundRepository.save(refund));
	}
	
	@Override
	public UserRefundDetailResponseDTO updateRefundStatus(UserRefundStatusUpdateDTO refundStatusUpdateDTO) {

        Refund refund = refundRepository.findById(refundStatusUpdateDTO.getRefundId())
                .orElseThrow(() -> new IllegalArgumentException("환불 요청을 찾을 수 없습니다."));
        
        //환불 승인인 경우 카카오페이 환불 처리
        if(refundStatusUpdateDTO.getNewStatus() == OrderStatus.REFUND_SUCCESS) {
        	try {
        		// 주문에 연결된 카카오페이 결제 정보 조회
        		KakaoPayment kakaopayment = kakaoPaymentRepository.findByOrderId(refund.getOrder().getId())
        				.orElseThrow(() -> new IllegalArgumentException("결제 정보를 찾을 수 없습니다."));
        		
        		// 카카오페이 환불 요청 생성
        		KakaoPayRefundRequestDTO kakaoPayRefundRequestDTO = KakaoPayRefundRequestDTO.builder()
        				.tid(kakaopayment.getTid())
        				.cancelAmount(refund.getOrder().getTotalAmount())
                        .cancelReason("관리자 승인")
                        .partnerOrderId(String.valueOf(refund.getOrder().getId()))
                        .partnerUserId(refund.getOrder().getMember().getId())
                        .build();
        		
        		// 카카오페이 환불 처리
        		kakaoPaymentService.refundPayment(kakaoPayRefundRequestDTO);
        		
                // OrderManagementService를 통한 주문 상태 업데이트
                orderManagementService.processRefundRequest(
                    refund.getOrder().getId(),
                    true,  // approve
                    "카카오페이 환불 완료"
                );
        	} catch(Exception e) {
                log.error("카카오페이 환불 처리 중 오류 발생", e);
                throw new RuntimeException("카카오페이 환불 처리에 실패했습니다.", e);
        	}
        } else if (refundStatusUpdateDTO.getNewStatus() == OrderStatus.REFUND_REJECTED) {
            // 환불 거절 처리
            orderManagementService.processRefundRequest(
                refund.getOrder().getId(),
                false,  // reject
                "관리자 거절"
            );
        }
        
        // 환불 상태 업데이트
        refund.updateStatus(refundStatusUpdateDTO.getNewStatus());
        
        // 주문 상태 업데이트
        refund.getOrder().setStatus(refundStatusUpdateDTO.getNewStatus().name());
        orderedRepository.save(refund.getOrder());
        
        return UserRefundDetailResponseDTO.fromEntity(refundRepository.save(refund));
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserRefundDetailResponseDTO getRefundDetails(Long refundId) {
	    
		Refund refund = refundRepository.findById(refundId)
	            .orElseThrow(() -> new IllegalArgumentException("환불 요청을 찾을 수 없습니다."));
	        
	        try {
	            // Order 정보 강제 초기화
	            refund.getOrder().getId();  // Order 정보 로딩
	            return UserRefundDetailResponseDTO.fromEntity(refund);
	        } catch (Exception e) {
	            log.error("환불 상세 정보 조회 중 오류 발생", e);
	            throw new IllegalArgumentException("환불 정보를 불러오는데 실패했습니다.");
	        }
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<UserRefundListResponseDTO> getRefundList(
			OrderStatus status,
			LocalDateTime startDate,
			LocalDateTime endDate,
			String search,
			Pageable pageable
	) {
	    log.info("Refund List Query Params: status={}, startDate={}, endDate={}, search={}, pageable={}", 
	             status, startDate, endDate, search, pageable);
		
	    return refundRepository.searchRefunds(status, startDate, endDate, search, pageable)
	            .map(UserRefundListResponseDTO::fromEntity);
	}
	@Override
	@Transactional(readOnly = true)
	public Page<UserRefundListResponseDTO> getMemberRefunds(String memberId, Pageable pageable) {
		Page<Refund> refundPage = refundRepository.findByOrderMemberIdOrderByRequestDateDesc(memberId, pageable);
        return refundPage.map(UserRefundListResponseDTO::fromEntity);
        }
}
