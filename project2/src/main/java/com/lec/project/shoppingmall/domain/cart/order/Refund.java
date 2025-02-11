package com.lec.project.shoppingmall.domain.cart.order;

import java.time.LocalDateTime;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Refund extends BaseEntity{
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Ordered order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private OrderStatus currentStatus = OrderStatus.REFUND_REQUESTED;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus previousStatus;

    @Column(length = 500)
    private String refundReason;

    private LocalDateTime processDate;

    @Builder.Default
    private LocalDateTime requestDate = LocalDateTime.now();

    // 환불 상태 업데이트 메서드
    public void updateStatus(OrderStatus newStatus) {
        if (newStatus == OrderStatus.REFUND_SUCCESS || 
            newStatus == OrderStatus.REFUND_REJECTED) {
            this.processDate = LocalDateTime.now();
        }
        this.currentStatus = newStatus;
    }
}
