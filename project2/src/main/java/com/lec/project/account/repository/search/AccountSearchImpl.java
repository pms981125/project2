package com.lec.project.account.repository.search;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.lec.project.account.domain.Account;
import com.lec.project.account.domain.QAccount;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class AccountSearchImpl extends QuerydslRepositorySupport implements AccountSearch{
	
	public AccountSearchImpl() {
		super(Account.class);
	}

	@Override
	public Page<Account> searchAllImpl(String[] types, String keyword, Pageable pageable) {
		QAccount account = QAccount.account;
		JPQLQuery<Account> query = from(account);
		
		if((types != null || types.length > 0) && keyword != null) {
			BooleanBuilder booleanBuilder = new BooleanBuilder();
			
			query.where(booleanBuilder);
		}
		this.getQuerydsl().applyPagination(pageable, query);
		List<Account> list = query.fetch();
		long count = query.fetchCount();
		
		return new PageImpl<>(list, pageable, count);
	}

}
