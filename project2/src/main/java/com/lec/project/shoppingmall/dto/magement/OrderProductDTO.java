package com.lec.project.shoppingmall.dto.magement;

import com.lec.project.shoppingmall.domain.cart.order.OrderedProduct;
import com.lec.project.shoppingmall.domain.product.productimage.ProductImage;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderProductDTO {
	
	private String productCode;
	private String productName;
	private int quantity;
	private int price;
	private int totalPrice;
	private String thumbnailPath;
	
	public static OrderProductDTO fromEntity(OrderedProduct orderedProduct) {
		
		// 대표 이미지 찾기
		 String thumbnailPath = orderedProduct.getProduct().getProductImages().stream()
			.filter(ProductImage::getIsMainImg)
			.findFirst()
			.map(ProductImage::getThumbnailPath)
			.orElse(null);
		 
			return OrderProductDTO.builder()
				.productCode(orderedProduct.getProduct().getProductCode())
				.productName(orderedProduct.getProduct().getProductName())
				.quantity(orderedProduct.getCount())
				.price(orderedProduct.getPrice())
				.totalPrice(orderedProduct.getTotalPrice())
				.thumbnailPath(thumbnailPath)
				.build();
	}
}
