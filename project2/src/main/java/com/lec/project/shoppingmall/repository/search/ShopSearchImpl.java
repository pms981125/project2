package com.lec.project.shoppingmall.repository.search;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.lec.project.shoppingmall.domain.QShop;
import com.lec.project.shoppingmall.domain.Shop;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ShopSearchImpl extends QuerydslRepositorySupport implements ShopSearch{

	public ShopSearchImpl() {
		super(Shop.class);
	}

	@Override
	public Page<Shop> searchAllImpl(String keyword, Pageable pageable) {
		QShop shop = QShop.shop;
		JPQLQuery<Shop> query = from(shop);
		
		if(keyword != null) {
			BooleanBuilder booleanBuilder = new BooleanBuilder();
			booleanBuilder.or(shop.gName.contains(keyword)); // 상품이름으로 검색
			
			query.where(booleanBuilder);
		}
		
		this.getQuerydsl().applyPagination(pageable, query);
		List<Shop> list = query.fetch();
		long count = query.fetchCount();
		
		return new PageImpl<>(list, pageable, count);
	}

}
