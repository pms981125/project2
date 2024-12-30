package com.lec.project.shoppingmall.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lec.project.shoppingmall.domain.cart.Cart;

public interface CartRepository extends JpaRepository<Cart, Long>{
	Cart findByMemberId(String memberId);
}
