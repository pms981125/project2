package com.lec.project.shoppingmall.dto.payment.kakao;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KakaoPayReadyResponse {
	private String tid;                  // 결제 고유 번호
	private String nextRedirectPcUrl;    // PC 웹 결제 페이지 URL
	private String nextRedirectMobileUrl;// 모바일 결제 페이지 URL
	private String nextRedirectAppUrl;   // 앱 결제 페이지 URL
	private LocalDateTime created_at;    // 결제 준비 요청 시간
}
