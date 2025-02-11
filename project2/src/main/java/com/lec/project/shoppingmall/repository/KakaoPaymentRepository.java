package com.lec.project.shoppingmall.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lec.project.shoppingmall.domain.payment.kakao.KakaoPayment;
import com.lec.project.shoppingmall.domain.payment.kakao.KakaoPaymentStatus;

@Repository
public interface KakaoPaymentRepository extends JpaRepository<KakaoPayment, Long> {
	Optional<KakaoPayment> findByTid(String tid);
	Optional<KakaoPayment> findByPartnerOrderId(String partnerOrderId);	
	List<KakaoPayment> findByPartnerUserIdOrderByCreatedAtDesc(String partnerUserId);
	List<KakaoPayment> findByStatus(KakaoPaymentStatus status);
	Optional<KakaoPayment> findByOrderId(Long orderId);
	
	@Query("SELECT k FROM KakaoPayment k WHERE k.partnerUserId = :userId AND k.status = :status")
	List<KakaoPayment> findByUserIdAndStatus(
		@Param("userId") String userId, 
		@Param("status") KakaoPaymentStatus status
	);
}