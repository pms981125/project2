package com.lec.project.shoppingmall.service.refund;

import java.awt.print.Pageable;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lec.project.shoppingmall.domain.cart.order.OrderStatus;
import com.lec.project.shoppingmall.domain.cart.order.Ordered;
import com.lec.project.shoppingmall.domain.cart.order.Refund;
import com.lec.project.shoppingmall.domain.refund.RefundListResponseDTO;
import com.lec.project.shoppingmall.domain.refund.RefundResponseDTO;
import com.lec.project.shoppingmall.domain.refund.RefundStatusUpdateDTO;
import com.lec.project.shoppingmall.dto.magement.RefundRequestDTO;
import com.lec.project.shoppingmall.repository.OrderedRepository;
import com.lec.project.shoppingmall.repository.RefundRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RefundServiceImpl implements RefundService {
//
//	private final RefundRepository refundRepository;
//	private final OrderedRepository orderedRepository;
//	
//	@Override
//	public RefundResponseDTO createRefund(String memberId, RefundRequestDTO refundRequestDTO) {
//		
//        Ordered ordered = orderedRepository.findById(refundRequestDTO.getOrderId())
//                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
//		
//        // 본인 확인
//        if (!ordered.getMember().getId().equals(memberId)) {
//            throw new IllegalArgumentException("본인의 주문만 환불 신청이 가능합니다.");
//        }
//        
//        // 환불 상태 확인
//        if (refundRepository.findByOrderId(ordered.getId()).isPresent()) {
//            throw new IllegalStateException("이미 환불이 신청된 주문입니다.");
//        }
//        
//        Refund refund = Refund.builder()
//                .order(ordered)
//                .previousStatus(OrderStatus.valueOf(ordered.getStatus()))
//                .refundReason(refundRequestDTO.getRefundReason())
//                .build();
//        
//        // 주문 상태 업데이트
//        ordered.setStatus(OrderStatus.REFUND_REQUESTED.name());
//        orderedRepository.save(ordered);
//        
//        return RefundResponseDTO.fromEntity(refundRepository.save(refund));
//	}
//	
//	@Override
//	public RefundResponseDTO updateRefundStatus(RefundStatusUpdateDTO refundStatusUpdateDTO) {
//
//        Refund refund = refundRepository.findById(refundStatusUpdateDTO.getRefundId())
//                .orElseThrow(() -> new IllegalArgumentException("환불 요청을 찾을 수 없습니다."));
//        
//        refund.updateStatus(refundStatusUpdateDTO.getNewStatus());
//        
//        // 주문 상태도 업데이트
//        refund.getOrder().setStatus(refundStatusUpdateDTO.getNewStatus().name());
//        orderedRepository.save(refund.getOrder());
//        
//        return RefundResponseDTO.fromEntity(refundRepository.save(refund));
//	}
//	
//	@Override
//	@Transactional(readOnly = true)
//	public RefundResponseDTO getRefundDetails(Long refundId) {
//        return RefundResponseDTO.fromEntity(
//                refundRepository.findById(refundId)
//                    .orElseThrow(() -> new IllegalArgumentException("환불 요청을 찾을 수 없습니다."))
//            );
//	}
//	
//	@Override
//	@Transactional(readOnly = true)
//	public Page<RefundListResponseDTO> getRefundList(
//			OrderStatus status,
//			LocalDateTime startDate,
//			LocalDateTime endDate,
//			String search,
//			Pageable pageable
//	) {
//		
//        return refundRepository.searchRefunds(status, startDate, endDate, search, pageable)
//                .map(RefundListResponseDTO::fromEntity);
//	}
//	@Override
//	@Transactional(readOnly = true)
//	public Page<RefundListResponseDTO> getMemberRefunds(String memberId, Pageable pageable) {
////        return refundRepository.findByMemberIdOrderByRequestDateDesc(memberId)
////                .stream()
////                .map(RefundListResponseDTO::fromEntity)
////                .collect(java.util.stream.Collectors.collectingAndThen(
////                    java.util.stream.Collectors.toList(),
////                    list -> new org.springframework.data.domain.PageImpl<>(list, pageable, list.size())
////                ));
//		return null;
//        }
}
