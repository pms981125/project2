package com.lec.project.shoppingmall.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lec.project.shoppingmall.domain.Shop;
import com.lec.project.shoppingmall.repository.search.ShopSearch;

public interface ShopRepository extends JpaRepository<Shop, Long>, ShopSearch{

}
