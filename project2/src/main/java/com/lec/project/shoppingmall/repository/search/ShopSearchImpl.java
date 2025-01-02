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
	public Page<Shop> searchAllImpl(String keyword
			, String category
			, Pageable pageable) {
	    QShop shop = QShop.shop;
	    QProduct product = QProduct.product;
	    log.info("SearchAllImpl - Category: {}, Keyword: {}", category, keyword);
	    
	    JPQLQuery<Shop> query = from(shop)
	        .leftJoin(shop.product, product).fetchJoin()
	        .select(shop);

	    BooleanBuilder booleanBuilder = new BooleanBuilder();
	    
	    //카테고리 검색
	    if(category != null && !category.trim().isEmpty()) {
	    	log.info("Filtering by category: {}", category);
	    	booleanBuilder.and(product.product_category.eq(category));
	    }
	    
	    //키워드 검색
	    if(keyword != null && !keyword.trim().isEmpty()) {
	    	log.info("Adding keyword condition: {}", keyword);
	    	booleanBuilder.and(
		            shop.board_title.containsIgnoreCase(keyword)
		            .or(product.product_name.containsIgnoreCase(keyword))
		        );
	    }
	    
	    
	    if(booleanBuilder.hasValue()) {
	    	query.where(booleanBuilder);
	    	log.info("Query conditions added");
	    }
	    
	    this.getQuerydsl().applyPagination(pageable, query);
	    
	    List<Shop> list = query.fetch();
	    long count = query.fetchCount();
	    
	    log.info("Total items found: {}", count);
	    log.info("Found shops: {}", list.size());
	    list.forEach(s -> {
	        log.info("Shop: bno={}, title={}, product={}", 
	            s.getBno(), 
	            s.getBoard_title(), 
	            s.getProduct() != null ? s.getProduct().getProduct_name() : "No Product"
	        );
	    });
	    
	    return new PageImpl<>(list, pageable, count);
	}

}
