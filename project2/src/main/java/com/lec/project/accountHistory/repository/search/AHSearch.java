package com.lec.project.accountHistory.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lec.project.accountHistory.domain.AccountHistory;

public interface AHSearch {
	Page<AccountHistory> searchAllImpl(String[] types, String keyword, Pageable pageable);
}
