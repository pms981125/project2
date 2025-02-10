package com.lec.project.shoppingmall.controller.cart.api;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
//version up
	private final CartService cartService;

	@PostMapping("/clear")
	public ResponseEntity<Void> clearCart(
			@AuthenticationPrincipal UserDetails userDetails) {
		String memberId = userDetails.getUsername();
	    cartService.removeAll(memberId);
	    
	    return ResponseEntity.ok().build();
	}

	@PostMapping("/add")
	public ResponseEntity<Object> addToCart(
			@AuthenticationPrincipal UserDetails userDetails,
			@RequestParam("productCode") String productCode,
			@RequestParam("count") int count
	) {
		 try {
		        String memberId = userDetails.getUsername();
		        cartService.addToCart(memberId, productCode, count);
		        return ResponseEntity.ok().build();
		    } catch (IllegalArgumentException e) {
		        // 재고 초과 시 메시지와 함께 400 상태 코드 반환
		        return ResponseEntity.badRequest().body(e.getMessage());
		    }
	}
	
	@PostMapping("/updateQuantity")
	public ResponseEntity<Object> updateQuantity(
			@AuthenticationPrincipal UserDetails userDetails,
			@RequestParam("id") Long id,
			@RequestParam("quantity") int quantity
			) {
		try {
			String memberId = userDetails.getUsername();
			cartService.modify(id, quantity, memberId);
			
			int newTotalPrice = cartService.getTotalPrice(memberId);
			return ResponseEntity.ok().body(Map.of(
					"success", true,
					"totalPrice", newTotalPrice
					));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(Map.of(
					"success", false,
					"message", e.getMessage()
			));
		}
	}
	
}