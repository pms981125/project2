package com.lec.project.accountHistory.service;

import java.util.List;

import com.lec.project.accountHistory.domain.AccountHistory;
import com.lec.project.accountHistory.dto.AccountHistoryDTO;
import com.lec.project.accountHistory.dto.PageRequestDTO;
import com.lec.project.accountHistory.dto.PageResponseDTO;

public interface AHService {


	void register(Long senderAccountId, Long receiverAccountId, int transferAmount);

	AccountHistoryDTO readOne(Long accountHistoryId);

	PageResponseDTO<AccountHistoryDTO> getListOfTransfer(PageRequestDTO pageRequestDTO);
	
	default AccountHistoryDTO entityToDTO(AccountHistory accountHistory) {
		AccountHistoryDTO accountHistoryDTO = AccountHistoryDTO.builder()
									.accountHistoryId(accountHistory.getAccountHistoryId())
									.accountId(accountHistory.getAccount().getAccountId())
									.transferTarget(accountHistory.getTransferTarget())
									.transferDate(accountHistory.getTransferDate())
									.transferAmount(accountHistory.getTransferAmount())
									.build();
									
		return accountHistoryDTO;
	}

	List<AccountHistoryDTO> getAllTransfers();

	PageResponseDTO<AccountHistoryDTO> getListOfBoard(Long accountId, PageRequestDTO pageRequestDTO);


}
