package com.lec.project.shoppingmall.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lec.project.shoppingmall.domain.Shop;

public interface ShopRepository extends JpaRepository<Shop, Long>{

}
