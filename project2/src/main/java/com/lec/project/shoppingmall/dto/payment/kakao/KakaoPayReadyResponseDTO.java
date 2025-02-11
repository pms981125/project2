package com.lec.project.shoppingmall.dto.payment.kakao;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KakaoPayReadyResponseDTO {
	private String tid;                  // 결제 고유 번호
	
	@JsonProperty("next_redirect_app_url")
	private String nextRedirectAppUrl;   // 앱 결제 페이지 URL
	
	@JsonProperty("next_redirect_mobile_url")
	private String nextRedirectMobileUrl;// 모바일 결제 페이지 URL
	
	@JsonProperty("next_redirect_pc_url")
	private String nextRedirectPcUrl;    // PC 웹 결제 페이지 URL
	
	@JsonProperty("created_at")
	private LocalDateTime createdAt;    // 결제 준비 요청 시간
}
