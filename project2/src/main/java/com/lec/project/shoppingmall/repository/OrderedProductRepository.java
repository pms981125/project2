package com.lec.project.shoppingmall.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lec.project.shoppingmall.domain.cart.order.Ordered;
import com.lec.project.shoppingmall.domain.cart.order.OrderedProduct;

public interface OrderedProductRepository extends JpaRepository<OrderedProduct, Long> {
    // 주문별 상품 목록 조회
    List<OrderedProduct> findByOrdered(Ordered ordered);
}
