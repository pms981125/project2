package com.lec.project.accountHistory.repository.search;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.lec.project.accountHistory.domain.AccountHistory;
import com.lec.project.accountHistory.domain.QAccountHistory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class AHSearchImpl extends QuerydslRepositorySupport implements AHSearch{

	public AHSearchImpl() {
		super(AccountHistory.class);
	}

	public Page<AccountHistory> searchAllImpl(String[] types, String keyword, Pageable pageable) {
		QAccountHistory accountHistory = QAccountHistory.accountHistory;
		JPQLQuery<AccountHistory> query = from(accountHistory);
		
		if((types != null || types.length > 0) && keyword != null) {
			BooleanBuilder booleanBuilder = new BooleanBuilder();
			
			query.where(booleanBuilder);
		}
		this.getQuerydsl().applyPagination(pageable, query);
		List<AccountHistory> list = query.fetch();
		long count = query.fetchCount();
		
		return new PageImpl<>(list, pageable, count);
	}
}
