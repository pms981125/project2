package com.lec.project.accountHistory.repository.search;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.lec.project.accountHistory.domain.AccountHistory;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class AHSearchImpl extends QuerydslRepositorySupport implements AHSearch{

	public AHSearchImpl() {
		super(AccountHistory.class);
	}

	public Page<AccountHistory> searchAllImpl(String[] types, String keyword, Pageable pageable) {
		return null;
	}
}
