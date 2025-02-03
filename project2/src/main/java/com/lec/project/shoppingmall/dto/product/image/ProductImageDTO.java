package com.lec.project.shoppingmall.dto.product.image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageDTO {

	private String imgId;
	private String originalImgName;
	private String storedImgName;
	private String imgPath;
	private String thumbnailPath;
	private Boolean isMainImg;
	private String productCode;
}
