package com.lec.project.account.service;

import java.util.List;

import com.lec.project.account.domain.Account;
import com.lec.project.account.dto.AccountDTO;
import com.lec.project.account.dto.PageRequestDTO;
import com.lec.project.account.dto.PageResponseDTO;

import jakarta.validation.Valid;

public interface AccountService {

	PageResponseDTO<AccountDTO> list(PageRequestDTO pageRequestDTO);

	AccountDTO readOne(Long accountId);
	
	Long register(AccountDTO accountDTO);

	default AccountDTO entityToDTO(Account account) {
		AccountDTO accountDTO = AccountDTO.builder()
				.accountId(account.getAccountId())
                .memberId(account.getMember().getId())
                .createDate(account.getCreateDate())
                .balance(account.getBalance())
                .build();
		
		return accountDTO;
						
	}
	
	void deleteAccount(Long accountId);

	void transfer(Long senderAccountId, Long receiverAccountId, int transferAmount);
	
	public boolean isAccountIdExists(Long accountId);
	
	public boolean hasAccountForCurrentUser();
	
	public List<AccountDTO> findAccountsByUsername(String username);
	
	void deposit(Long accountId, int depositAmount);
	
	public boolean hasManagerAccount();
}
