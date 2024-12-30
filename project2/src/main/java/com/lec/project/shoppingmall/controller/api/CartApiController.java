package com.lec.project.shoppingmall.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lec.project.shoppingmall.service.cart.CartService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Log4j2
public class CartApiController {

	private final CartService cartService;

	@PostMapping("/clear")
	public ResponseEntity<Void> clearCart() {
		cartService.removeAll();
		return ResponseEntity.ok().build();
	}

	@PostMapping("/add")
	public ResponseEntity<Void> addToCart(
			@RequestParam("productCode") String productCode,
			@RequestParam("count") int count) {
		try {
			cartService.addToCart(productCode, count);
			return ResponseEntity.ok().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		}
	}
}