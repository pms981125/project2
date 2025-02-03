package com.lec.project.shoppingmall.dto.cart.order;

import lombok.Data;

@Data
public class OrderSubmitDTO {
	private String customerName;
	private String phoneNumber;
	private String location;
	private String address;
	private String specialRequests;
	private int totalAmount;
}
