package com.lec.project.shoppingmall.repository.search;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.lec.project.shoppingmall.domain.product.QProduct;
import com.lec.project.shoppingmall.domain.shop.QShop;
import com.lec.project.shoppingmall.domain.shop.Shop;
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
	    QProduct product = QProduct.product;
	    
	    JPQLQuery<Shop> query = from(shop)
	        .leftJoin(shop.product, product).fetchJoin()
	        .select(shop);
	    
	    if(keyword != null) {
	        BooleanBuilder booleanBuilder = new BooleanBuilder();
	        booleanBuilder.or(shop.board_title.contains(keyword));
	        query.where(booleanBuilder);
	    }
	    
	    this.getQuerydsl().applyPagination(pageable, query);
	    List<Shop> list = query.fetch();
	    long count = query.fetchCount();
	    
	    return new PageImpl<>(list, pageable, count);
	}

}
