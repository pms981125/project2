package com.lec.project.account.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
private Long accountId;
    
    @NotNull(message = "회원 ID는 필수입니다")
    private String memberId;
    
    private String memberName;
    
    private LocalDateTime createDate;

    private int balance;
}
