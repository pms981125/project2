package com.lec.project.accountHistory.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.lec.project.account.domain.Account;
import com.lec.project.account.dto.AccountDTO;
import com.lec.project.account.repository.AccountRepository;
import com.lec.project.accountHistory.domain.AccountHistory;
import com.lec.project.accountHistory.dto.AccountHistoryDTO;
import com.lec.project.accountHistory.dto.PageRequestDTO;
import com.lec.project.accountHistory.dto.PageResponseDTO;
import com.lec.project.accountHistory.repository.AHRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class AHServiceImpl implements AHService{
	private final ModelMapper modelMapper;
	private final AHRepository ahRepository;
	public AccountRepository accountRepository;

	@Override
	public PageResponseDTO<AccountHistoryDTO> getListOfTransfer(PageRequestDTO pageRequestDTO) {
		String[] types = pageRequestDTO.getTypes();
		String keyword = pageRequestDTO.getKeyword();
		Pageable pageable = pageRequestDTO.getPageable("accountHistoryId");
		
		Page<AccountHistory> result = pageRequestDTO.searchAllImpl(types, keyword, pageable);
		List<AccountHistoryDTO> dtoList = result.getContent()
								.stream()
								.map(ah -> modelMapper.map(ah, AccountHistoryDTO.class))
								.collect(Collectors.toList());
		
		return PageResponseDTO.<AccountHistoryDTO>withAll()
				.pageRequestDTO(pageRequestDTO)
				.dtoList(dtoList)
				.total((int) result.getTotalElements())
				.build();
	}

	@Override
	public PageResponseDTO<AccountHistoryDTO> getListOfBoard(Long accountId, PageRequestDTO pageRequestDTO) {
	    Pageable pageable = PageRequest.of(
	        pageRequestDTO.getPage() <= 0 ? 0 : pageRequestDTO.getPage() - 1,
	        pageRequestDTO.getSize(),
	        Sort.by("accountHistoryId").descending()
	    );

	    Page<AccountHistory> result = ahRepository.findByAccountAccountIdOrTransferTarget(accountId, accountId, pageable);
	    
	    List<AccountHistoryDTO> dtoList = result.getContent()
	        .stream()
	        .map(ah -> {
	            AccountHistoryDTO dto = new AccountHistoryDTO();
	            dto.setAccountHistoryId(ah.getAccountHistoryId());
	            dto.setAccountId(ah.getAccount().getAccountId());
	            dto.setTransferTarget(ah.getTransferTarget());
	            dto.setTransferAmount(ah.getTransferAmount());
	            dto.setTransferDate(ah.getTransferDate());

	            // transactionType이 이미 설정되어 있으면 그 값을 사용
	            if (ah.getTransactionType() != null) {
	                dto.setTransactionType(ah.getTransactionType());
	                dto.setAmount(ah.getTransferAmount());
	            } else {
	                // 기존 로직 유지 (이전 데이터 호환성을 위해)
	                if (ah.getAccount().getAccountId().equals(accountId)) {
	                    dto.setTransactionType("출금");
	                    dto.setAmount(-ah.getTransferAmount());
	                } else {
	                    dto.setTransactionType("입금");
	                    dto.setAmount(ah.getTransferAmount());
	                }
	            }
	            
	            return dto;
	        })
	        .collect(Collectors.toList());

	    return PageResponseDTO.<AccountHistoryDTO>withAll()
	        .pageRequestDTO(pageRequestDTO)
	        .dtoList(dtoList)
	        .total((int) result.getTotalElements())
	        .build();
	}
	
	@Override
	public void register(@RequestParam("senderAccountId") Long senderAccountId,
			 @RequestParam("receiverAccountId") Long receiverAccountId,
			 @RequestParam("transferAmount") int transferAmount) {
		if (transferAmount <= 0) {
	        throw new IllegalArgumentException("Transfer amount must be greater than zero");
	    }
		
		 // 송금 계좌 확인
        Account senderAccount = accountRepository.findByAccountId(senderAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Sender account not found with ID: " + senderAccountId));

        // 수신 계좌 확인
        Account receiverAccount = accountRepository.findByAccountId(receiverAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Receiver account not found with ID: " + receiverAccountId));

        // 송금 계좌 잔액 확인
        if (senderAccount.getBalance() < transferAmount) {
            throw new IllegalArgumentException("잔액이 부족합니다");
        }

        // 송금 계좌 잔액 차감
        senderAccount.setBalance(senderAccount.getBalance() - transferAmount);

        // 수신 계좌 잔액 추가
        receiverAccount.setBalance(receiverAccount.getBalance() + transferAmount);

        // 계좌 정보 저장
        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        // 이체 내역 저장
        AccountHistory history = new AccountHistory();
        history.setAccount(senderAccount);
        history.setTransferTarget(receiverAccount.getAccountId());
        history.setTransferAmount(transferAmount);

        ahRepository.save(history);
	}

	@Override
	public AccountHistoryDTO readOne(Long accountHistoryId) {
		Optional<AccountHistory> result = ahRepository.findById(accountHistoryId);
		AccountHistory accountHistory = result.orElseThrow();
		AccountHistoryDTO accountHistoryDTO = entityToDTO(accountHistory);
		return accountHistoryDTO;
	}

	@Override
	public List<AccountHistoryDTO> getAllTransfers() {
		 List<AccountHistory> accountHistories = ahRepository.findAll();
	     return accountHistories.stream()
	                .map(accountHistory -> modelMapper.map(accountHistory, AccountHistoryDTO.class))
	                .toList();
	}


}