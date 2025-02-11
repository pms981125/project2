package com.lec.project.shoppingmall.domain.cart.order;

public enum OrderStatus {
    PENDING("대기"),
    APPROVED("승인"),
    REJECTED("거절"),
	REFUND_REQUESTED("환불 요청"),
	REFUND_SUCCESS("환불 완료"),
    REFUND_REJECTED("환불 거절");
	
    private final String description;
    
    OrderStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
	
}
