package com.lec.project.shoppingmall.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lec.project.shoppingmall.domain.shop.Shop;

public interface ShopSearch {
	Page<Shop> searchAllImpl(String keyword, String category, Pageable pageable);
}
