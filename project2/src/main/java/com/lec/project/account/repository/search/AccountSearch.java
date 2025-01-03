package com.lec.project.account.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lec.project.account.domain.Account;

public interface AccountSearch {
	Page<Account> searchAllImpl(String[] types, String keyword, Pageable pageable);
}
