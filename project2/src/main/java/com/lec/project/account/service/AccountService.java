package com.lec.project.account.service;

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
						.createDate(account.getCreateDate())
						.balance(0)
						.build();
		
		return accountDTO;
						
	}

	void transfer(Long senderAccountId, Long receiverAccountId, int transferAmount);
}
