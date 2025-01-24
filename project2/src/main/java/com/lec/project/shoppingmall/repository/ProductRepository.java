package com.lec.project.shoppingmall.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lec.project.shoppingmall.domain.product.Product;

public interface ProductRepository extends JpaRepository<Product, String>{

}
