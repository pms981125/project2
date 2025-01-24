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

	private String img_id;
	private String original_img_name;
	private String stored_img_name;
	private String img_path;
	private String thumbnail_path;
	private Boolean is_main_img;
	private String product_code;
}
