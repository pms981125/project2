package com.lec.project.shoppingmall.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lec.project.shoppingmall.domain.cart.Cart;


public interface CartRepository extends JpaRepository<Cart, Long>{
	Optional<Cart> findByMemberId(String memberId);
}
