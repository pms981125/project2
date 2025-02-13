package com.lec.project.shoppingmall.service.cart.order;

import com.lec.project.shoppingmall.dto.cart.order.OrderSubmitDTO;
import com.lec.project.shoppingmall.dto.magement.OrderManagementDTO;

public interface OrderService {
	void createOrder(String memberId, OrderSubmitDTO orderSubmitDTO);	
	void requestRefund(Long orderId);
	OrderManagementDTO cancelRefund(Long orderId, String memberId);
}
