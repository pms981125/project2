package com.lec.project.account.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.lec.project.Member;
import com.lec.project.accountHistory.domain.AccountHistory;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "accountHistories") // 순환참조 방지를 위해 accountHistories 제외
public class Account extends BaseEntity {

    @Id
    private Long accountId;

    private int balance;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default  // Builder 패턴 사용시 초기화를 위해 추가
    private List<AccountHistory> accountHistories = new ArrayList<>();
}


