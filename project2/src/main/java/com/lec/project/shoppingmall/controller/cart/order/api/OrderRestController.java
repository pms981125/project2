package com.lec.project.shoppingmall.controller.cart.order.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lec.project.shoppingmall.service.cart.order.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Log4j2
public class OrderRestController {
    
    private final OrderService orderService;
    
    @PostMapping("/{orderId}/refund")
    public ResponseEntity<String> requestRefund(
        @PathVariable("orderId") Long orderId
    ) {
        try {
            orderService.requestRefund(orderId);
            return ResponseEntity.ok("환불이 요청되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("주문을 찾을 수 없습니다.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("환불 요청 처리 중 오류가 발생했습니다.");
        }
    }
}
