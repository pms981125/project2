package com.lec.project.account.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.lec.project.Member;
import com.lec.project.account.domain.Account;
import com.lec.project.account.dto.AccountDTO;
import com.lec.project.account.dto.PageRequestDTO;
import com.lec.project.account.dto.PageResponseDTO;
import com.lec.project.account.repository.AccountRepository;
import com.lec.project.accountHistory.domain.AccountHistory;
import com.lec.project.accountHistory.repository.AHRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService{
	private final ModelMapper modelMapper;
	private final AccountRepository accountRepository;
	private final AHRepository ahRepository;

	@Override
	public PageResponseDTO<AccountDTO> list(PageRequestDTO pageRequestDTO) {
	    // 현재 로그인한 사용자의 정보 가져오기
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String currentMemberId = authentication.getName();
	    
	    Pageable pageable = pageRequestDTO.getPageable("createDate");
	    
	    // 현재 사용자의 계좌만 조회
	    Page<Account> result = accountRepository.findByMemberId(currentMemberId, pageable);
	    
	    List<AccountDTO> dtoList = result.getContent()
	            .stream()
	            .map(account -> modelMapper.map(account, AccountDTO.class))
	            .collect(Collectors.toList());
	    
	    return PageResponseDTO.<AccountDTO>withAll()
	            .pageRequestDTO(pageRequestDTO)
	            .dtoList(dtoList)
	            .total((int) result.getTotalElements())
	            .build();
	}

	@Override
	public AccountDTO readOne(Long accountId) {
		Optional<Account> result = accountRepository.findByAccountId(accountId);
		Account account = result.orElseThrow();
		AccountDTO accountDTO = entityToDTO(account);
		return accountDTO;
	}

	@Override
    public Long register(AccountDTO accountDTO) {
        // 현재 로그인한 사용자의 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentMemberId = authentication.getName();  // 현재 로그인한 사용자의 ID
        
        Account account = modelMapper.map(accountDTO, Account.class);
        
        // Member 객체 생성 및 설정
        Member member = Member.builder()
                            .id(currentMemberId)
                            .build();
        account.setMember(member);
        
        return accountRepository.save(account).getAccountId();
    }
	
	@Override
	@Transactional
	public void transfer(@RequestParam("senderAccountId") Long senderAccountId,
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
    public boolean isAccountIdExists(Long accountId) {
        return accountRepository.findByAccountId(accountId).isPresent();
    }
	
	@Override
	public boolean hasAccountForCurrentUser() {
	    // 현재 로그인한 사용자의 계좌 존재 여부 확인 로직
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String username = auth.getName();
	    // 사용자의 계좌 목록을 조회하여 하나라도 있는지 확인
	    return !accountRepository.findByMemberId(username).isEmpty();
	}
	
	@Override
    public List<AccountDTO> findAccountsByUsername(String username) {
        // username으로 계좌 목록을 조회
        List<Account> accounts = accountRepository.findByMemberId(username);
        
        // Account 엔티티를 AccountDTO로 변환
        return accounts.stream()
                .map(account -> modelMapper.map(account, AccountDTO.class))
                .collect(Collectors.toList());
    }
	
	@Override
	@Transactional
	public void deleteAccount(Long accountId) {
	    Account account = accountRepository.findByAccountId(accountId)
	            .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + accountId));
	            
	    // 현재 로그인한 사용자 확인
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String currentMemberId = authentication.getName();
	    
	    // 계좌 소유자 확인
	    if (!account.getMember().getId().equals(currentMemberId)) {
	        throw new IllegalArgumentException("You don't have permission to delete this account");
	    }
	    
	    accountRepository.delete(account);
	}
	
	@Override
	@Transactional
	public void deposit(Long accountId, int depositAmount) {
	    if (depositAmount <= 0) {
	        throw new IllegalArgumentException("Deposit amount must be greater than zero");
	    }
	    
	    Account account = accountRepository.findByAccountId(accountId)
	            .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + accountId));
	            
	    // 입금처리
	    account.setBalance(account.getBalance() + depositAmount);
	    accountRepository.save(account);
	    
	    // 입금 내역 저장 - Builder 패턴 사용으로 변경
	    AccountHistory history = AccountHistory.builder()
	        .account(account)
	        .transferTarget(accountId)
	        .transferAmount(depositAmount)
	        .transactionType("입금")  
	        .build();
	    
	    ahRepository.save(history);
	}
	
	@Override
	public boolean hasManagerAccount() {
	    return !accountRepository.findByMemberId("manager").isEmpty();
	}
}
