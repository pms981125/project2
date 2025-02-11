package com.lec.project.shoppingmall.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lec.project.shoppingmall.domain.product.Product;

public interface ProductRepository extends JpaRepository<Product, String>{
	
    // 상품 코드로 부분 검색 (Containing)
    List<Product> findByProductCodeContaining(String productCode);

}
