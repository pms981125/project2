package com.lec.project.shoppingmall.service.refund;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lec.project.shoppingmall.domain.cart.order.OrderStatus;
import com.lec.project.shoppingmall.domain.cart.order.Ordered;
import com.lec.project.shoppingmall.domain.refund.Refund;
import com.lec.project.shoppingmall.dto.refund.UserRefundDetailResponseDTO;
import com.lec.project.shoppingmall.dto.refund.UserRefundListResponseDTO;
import com.lec.project.shoppingmall.dto.refund.UserRefundRequestDTO;
import com.lec.project.shoppingmall.dto.refund.UserRefundStatusUpdateDTO;
import com.lec.project.shoppingmall.repository.OrderedRepository;
import com.lec.project.shoppingmall.repository.RefundRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class RefundServiceImpl implements RefundService {

	private final RefundRepository refundRepository;
	private final OrderedRepository orderedRepository;
	
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
        
        refund.updateStatus(refundStatusUpdateDTO.getNewStatus());
        
        // 주문 상태도 업데이트
        refund.getOrder().setStatus(refundStatusUpdateDTO.getNewStatus().name());
        orderedRepository.save(refund.getOrder());
        
        return UserRefundDetailResponseDTO.fromEntity(refundRepository.save(refund));
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserRefundDetailResponseDTO getRefundDetails(Long refundId) {
        return UserRefundDetailResponseDTO.fromEntity(
                refundRepository.findById(refundId)
                    .orElseThrow(() -> new IllegalArgumentException("환불 요청을 찾을 수 없습니다."))
            );
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
	    
	    if (status == null) {
	        status = OrderStatus.REFUND_REQUESTED;
	    }
		
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
