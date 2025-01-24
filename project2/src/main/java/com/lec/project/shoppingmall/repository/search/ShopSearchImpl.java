package com.lec.project.shoppingmall.repository.search;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.lec.project.shoppingmall.domain.shop.Shop;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Repository
public class ShopSearchImpl implements ShopSearch{

	@PersistenceContext
	private EntityManager em;

	@Override
	public Page<Shop> searchAllImpl(String keyword
			, String category
			, Pageable pageable) {
		log.info("SearchAllImpl - Category: {}, Keyword: {}", category, keyword);
	    
		String baseQuery = "SELECT DISTINCT s FROM Shop s LEFT JOIN FETCH s.product p";
		String countQuery = "SELECT COUNT(DISTINCT s) FROM Shop s LEFT JOIN s.product p";

		StringBuilder where= new StringBuilder();
        
		if(category != null && !category.trim().isEmpty()) {
			where.append(" WHERE p.product_category = :category");
			if(keyword != null && !keyword.trim().isEmpty()) {
				where.append(" AND (s.board_title LIKE :keyword OR p.product_name LIKE :keyword)");
			}
		} else if(keyword != null && !keyword.trim().isEmpty()) {
			where.append(" WHERE s.board_title LIKE :keyword OR p.product_name LIKE :keyword");
		}
		
		String orderBy = " ORDER BY s.bno DESC";
        
		TypedQuery<Shop> query = em.createQuery(baseQuery + where + orderBy, Shop.class);
		TypedQuery<Long> countResult = em.createQuery(countQuery + where, Long.class);
        
//		파라미터 바인딩 로직
		if(category != null && !category.trim().isEmpty()) {
			query.setParameter("category", category);
			countResult.setParameter("category", category);
		}
		if(keyword != null && !keyword.trim().isEmpty()) {
			String likeKeyword = "%" + keyword + "%";
			query.setParameter("keyword", likeKeyword);
			countResult.setParameter("keyword", likeKeyword);
		}
        
		//페이징 처리
		query.setFirstResult((int) pageable.getOffset());
		query.setMaxResults(pageable.getPageSize());

		List<Shop> result = query.getResultList();
		long total = countResult.getSingleResult();
		
		return new PageImpl<>(result, pageable, total);
	}
}
