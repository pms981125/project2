package com.lec.project.shoppingmall.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lec.project.shoppingmall.domain.Shop;

public interface ShopSearch {

	Page<Shop> searchLike(Pageable pageable);
	Page<Shop> searchPageable(Pageable pageable);
	Page<Shop> searchBooleanBuilder(Pageable pageable);
	Page<Shop> searchAll(String keyword, Pageable pageable);
	Page<Shop> searchAllImpl(String keywString, Pageable pageable);
}
