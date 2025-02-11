package com.lec.project.shoppingmall.service.cart.order;

import com.lec.project.shoppingmall.dto.cart.order.OrderSubmitDTO;

public interface OrderService {
	void createOrder(String memberId, OrderSubmitDTO orderSubmitDTO);	
}
