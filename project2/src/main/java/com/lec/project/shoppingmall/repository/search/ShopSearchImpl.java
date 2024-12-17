package com.lec.project.shoppingmall.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.lec.project.shoppingmall.domain.Shop;

public class ShopSearchImpl extends QuerydslRepositorySupport implements ShopSearch{

	public ShopSearchImpl() {
		super(Shop.class);
	}

	@Override
	public Page<Shop> searchLike(Pageable pageable) {
		
		//QShop shop = QShop.shop;
		return null;
	}

	@Override
	public Page<Shop> searchPageable(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Shop> searchBooleanBuilder(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Shop> searchAll(String keyword, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Shop> searchAllImpl(String keywString, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

}
