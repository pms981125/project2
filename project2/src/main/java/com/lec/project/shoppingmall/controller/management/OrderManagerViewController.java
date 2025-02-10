package com.lec.project.shoppingmall.controller.management;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lec.project.shoppingmall.dto.magement.OrderManagementDTO;
import com.lec.project.shoppingmall.service.magement.OrderManagementService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/manager/orders")
@RequiredArgsConstructor
@Log4j2
public class OrderManagerViewController {

	private final OrderManagementService orderManagementService;
	
	@GetMapping
	public String orderManagementView(
		@RequestParam(required = false, name = "status") String status,    
		@RequestParam(required = false, name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,    
		@RequestParam(required = false, name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,    
		@RequestParam(required = false, name = "search") String search,    
		@PageableDefault(size = 10) Pageable pageable,
		Model model
	) {
		
		//주문 목록 조회
		Page<OrderManagementDTO> orderList = orderManagementService.getOrderList(
			status, startDate, endDate, search, pageable
		);
		
		model.addAttribute("orderList", orderList);
		
		//주문 상태 통계
		model.addAttribute("statusStatistics", orderManagementService.getOrderStatusStatistics());
		return "management/orderManagement";
	}
	
	//주문 상태 변경 
	
}
