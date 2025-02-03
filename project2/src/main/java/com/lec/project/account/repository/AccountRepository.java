package com.lec.project.account.repository;

import java.util.List;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lec.project.account.domain.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
	@Query("select a from Account a where a.accountId = :accountId")
	Optional<Account> findByAccountId(@Param("accountId") Long accountId);
	
	@Query("select a from Account a where a.member.id = :memberId")
	Page<Account> findByMemberId(@Param("memberId") String memberId, Pageable pageable);
	
	@Query("select a from Account a where a.member.id = :memberId")
	List<Account> findByMemberId(@Param("memberId") String memberId);
}
