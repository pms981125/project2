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
public class SecondHandMarket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId; // 게시글 ID

    private String title;       // 게시글 제목
    private String content;     // 게시글 내용
    private Double price;       // 상품 가격

    private String region;      // 거래 지역
    
    private String thumbnail;   // 썸네일 이미지 URL
    private String contentImage; // 본문 이미지 URL

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;      // 작성자 정보
}
