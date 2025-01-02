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
	private String product_code;
	
	@NotEmpty//게시판에 들어갈 상품이름
	private String board_title;
	
	// Product 관련 필드 추가
    private int product_price;
    private String product_category;
    private int product_stock;
    
    // 게시글 내용 필드 추가
    private String board_content1;
    private String board_content2;
    
    // 이미지 관련 필드 추가
    private String img_path;
    private String thumbnail_path;
}
