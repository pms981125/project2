package com.lec.project.shoppingmall.dto.shop;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopDTO {

	
	private Long bno;
	
	@NotEmpty//게시판에 들어갈 상품코드
	private String productCode;
	
	@NotEmpty//게시판에 들어갈 상품이름
	private String boardTitle;
	
	// Product 관련 필드 추가
	private int productPrice;
	private String productCategory;
	private int productStock;
    
    // 게시글 내용 필드 추가
	private String firstBoardContent;
	private String secondBoardContent; 
	
    // 이미지 관련 필드 추가
	private String imgPath;
	private String thumbnailPath;
}
