package com.lec.project.shoppingmall.dto.cart.order;

import lombok.Data;

@Data
public class OrderSubmitDTO {
	private String customer_name;
	private String phone_number;
	private String location;
	private String address;
	private String special_requests;
	private int total_amount;
}
