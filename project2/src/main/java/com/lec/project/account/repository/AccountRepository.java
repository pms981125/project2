package com.lec.project.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lec.project.account.domain.Account;
import com.lec.project.account.repository.search.AccountSearch;

public interface AccountRepository extends JpaRepository<Account, Long>, AccountSearch{
	@Query("select a from Account a where a.accountId = :accountId")
	Optional<Account> findByAccountId(@Param("accountId") Long accountId);
	

}
