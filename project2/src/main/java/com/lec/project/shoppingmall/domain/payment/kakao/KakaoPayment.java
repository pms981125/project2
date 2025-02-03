package com.lec.project.shoppingmall.domain.payment.kakao;

import java.time.LocalDateTime;

import com.lec.project.shoppingmall.domain.cart.order.Ordered;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "kakao_payment")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KakaoPayment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String tid;	//카카오페이 고유 결제번호
	
	@Column(nullable = false)
	private String partnerOrderId;	//가맹점 주문번호
	
	@Column(nullable = false)
	private String partnerUserId;	//가맹점 회원 ID
	
	@Column(nullable = false)
	private int totalAmount;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private KakaoPaymentStatus status;	//결제상태
	
	private String itemName;	// 상품명
	private int quantity;		// 수량
	private int taxFreeAmount;	// 비과세 금액
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Ordered order;		// 주문정보
	
	private LocalDateTime approvedAt;	// 결제 승인 시간
	private LocalDateTime cancelledAt;	// 결제 취소 시간
    
	@Column(updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
	
}