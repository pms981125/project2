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
		
		Page<AccountHistory> result = ahRepository.searchAllImpl(types, keyword, pageable);
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
	public Long register(AccountHistoryDTO accountHistoryDTO) {
		AccountHistory accountHistory = modelMapper.map(accountHistoryDTO, AccountHistory.class);
		Long accountHistoryId = ahRepository.save(accountHistory).getAccountHistoryId();
		return accountHistoryId;
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
