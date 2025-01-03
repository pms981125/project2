package com.lec.project.accountHistory.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lec.project.accountHistory.domain.AccountHistory;
import com.lec.project.accountHistory.repository.search.AHSearch;

public interface AHRepository extends JpaRepository<AccountHistory, Long>, AHSearch{

	@Query("select ah from AccountHistory ah where ah.account.accountId = :accountId")
	Page<AccountHistory> listOfBoard(@Param("accountId") Long accountId, Pageable pageable);

	

}
