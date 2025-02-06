package com.lec.project.shoppingmall.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lec.project.shoppingmall.domain.cart.Cart;
import com.lec.project.shoppingmall.domain.cart.CartProduct;
import com.lec.project.shoppingmall.domain.product.Product;

public interface CartProductRepository extends JpaRepository<CartProduct, Long>{
	Page<CartProduct> findByCart(Cart cart, Pageable pageable);
	List<CartProduct> findByCart(Cart cart);
	Optional<CartProduct> findByCartAndProduct(Cart cart, Product product);
	void deleteByCart(Cart cart);
}
