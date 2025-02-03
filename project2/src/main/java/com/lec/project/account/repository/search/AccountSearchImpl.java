package com.lec.project.account.repository.search;

import java.util.List;


import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.lec.project.account.domain.Account;


import lombok.extern.log4j.Log4j2;

@Log4j2
public class AccountSearchImpl extends QuerydslRepositorySupport implements AccountSearch{
	
	public AccountSearchImpl() {
		super(Account.class);
	}

	@Override
	public Page<Account> searchAllImpl(String[] types, String keyword, Pageable pageable) {
		return null;
	}

}
