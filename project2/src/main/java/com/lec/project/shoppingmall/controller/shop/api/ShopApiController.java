package com.lec.project.shoppingmall.controller.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lec.project.shoppingmall.repository.ShopRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Log4j2
@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
public class ShopApiController {

	private final ShopRepository shopRepository;
	
	@GetMapping("checkProductCode")
	public ResponseEntity<Map<String, Boolean>> checkProductCodeExist
			(@RequestParam(name = "productCode") String productCode){
		
		boolean exists = shopRepository.existsByProductCode(productCode);
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("exists", exists);
		return ResponseEntity.ok(response);
	}
	
	
}
