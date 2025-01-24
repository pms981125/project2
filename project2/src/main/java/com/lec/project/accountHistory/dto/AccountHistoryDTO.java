package com.lec.project.accountHistory.dto;

import java.time.LocalDateTime;


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
    private Long accountId;           // account 객체 대신 accountId만 사용
    private Long transferTarget;
    private Integer transferAmount;
    private LocalDateTime transferDate;
    private String transactionType;   // 입금/출금 구분
    private Integer amount;           // 실제 표시될 금액
}
