package com.lec.project.regionboard.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import com.lec.project.Member;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberRegion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키 자동 생성
    private Long regionId;

    @OneToOne // Member와 1:1 관계 설정
    @JoinColumn(name = "member_id", referencedColumnName = "id") // 외래 키 설정
    private Member member;

    private String region; // 지역 정보
}