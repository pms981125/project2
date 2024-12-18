package com.lec.project.shoppingmall.repository.search;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.lec.project.shoppingmall.domain.QShop;
import com.lec.project.shoppingmall.domain.Shop;
import com.querydsl.jpa.JPQLQuery;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ShopSearchImpl extends QuerydslRepositorySupport implements ShopSearch{

	public ShopSearchImpl() {
		super(Shop.class);
	}

	@Override
	public Page<Shop> searchLike(Pageable pageable) {
		
		QShop shop = QShop.shop;
		JPQLQuery<Shop> query = from(shop);		// select * from shop;
		query.where(shop.gName.contains("1"));	// where title like '%1%';
		List<Shop> list = query.fetch();
		long count = query.fetchCount();
		log.info("검색건수 = " + count);
		return null;
	}

	@Override
	public Page<Shop> searchPageable(Pageable pageable) {
		
		QShop shop = QShop.shop;
		JPQLQuery<Shop> query = from(shop);
		query.where(shop.gName.contains("1"))
			.limit(1)
			.offset(2);
		
		this.getQuerydsl().applyPagination(pageable, query);
		List<Shop> list = query.fetch();
		long count = query.limit(1).offset(2).fetchCount();
		log.info("검색건수 = " + count);
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
