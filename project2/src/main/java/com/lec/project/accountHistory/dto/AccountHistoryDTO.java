package com.lec.project.accountHistory.dto;

import java.time.LocalDateTime;

import com.lec.project.account.dto.AccountDTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountHistoryDTO {
	
	private Long accountHistoryId;
	
	private LocalDateTime transferDate;
	
    private Long transferTarget;
	
	@NotEmpty
    private int transferAmount;
	
}
