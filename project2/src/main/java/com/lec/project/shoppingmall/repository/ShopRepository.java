package com.lec.project.shoppingmall.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lec.project.shoppingmall.domain.shop.Shop;
import com.lec.project.shoppingmall.repository.search.ShopSearch;

public interface ShopRepository extends JpaRepository<Shop, Long>, ShopSearch{
	@Query("SELECT COUNT(s) > 0 FROM Shop s WHERE s.product_code = :productCode")
	boolean existsByProductCode(@Param("productCode") String productCode);
	
	@Query("SELECT s FROM Shop s WHERE s.product_code = :productCode")
	Optional<Shop> findByProduct_code(@Param("productCode") String productCode);
}
