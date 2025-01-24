package com.lec.project.shoppingmall.domain.product.productimage;

import com.lec.project.shoppingmall.domain.product.Product;

import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage {

	@Id
	@Column(columnDefinition = "CHAR(36)")
	@Builder.Default
	private String img_id = UUID.randomUUID().toString();
	
	// 원본 파일명
	@Column(nullable = false)
	private String original_img_name;
	
	// 서버에 저장된 파일명
	@Column(nullable = false)
	private String stored_img_name;
	
	// 이미지 저장 경로
	@Column(nullable = false)
	private String img_path;
	
	// 썸네일 경로
	@Column(nullable = false)
	private String thumbnail_path;
	
	// 대표 이미지 존재 여부
	@Column(nullable = false)
	@Builder.Default
	private Boolean is_main_img = false;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_code", nullable = false)
    private Product product;
	
	public static ProductImage createProductImage(
				String originalFileName
				, String storedFileName
				, String imgPath
				, String thumbnailPath
				, Product product
				, Boolean isMainImg
			) {
			return ProductImage.builder()
					.original_img_name(originalFileName)
					.stored_img_name(storedFileName)
					.img_path(imgPath)
					.thumbnail_path(thumbnailPath)
					.product(product)
					.is_main_img(isMainImg)
					.build();
		}
	
	 public void setAsMainImage() {
	        this.is_main_img = true;
	    }
}
