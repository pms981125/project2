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
	private String imgId = UUID.randomUUID().toString();
	
	// 원본 파일명
	@Column(nullable = false)
	private String originalImgName;
	
	// 서버에 저장된 파일명
	@Column(nullable = false)
	private String storedImgName;
	
	// 이미지 저장 경로
	@Column(nullable = false)
	private String imgPath;
	
	// 썸네일 경로
	@Column(nullable = false)
	private String thumbnailPath;
	
	// 대표 이미지 존재 여부
	@Column(nullable = false)
	@Builder.Default
	private Boolean isMainImg = false;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productCode", nullable = false)
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
					.originalImgName(originalFileName)
					.storedImgName(storedFileName)
					.imgPath(imgPath)
					.thumbnailPath(thumbnailPath)
					.product(product)
					.isMainImg(isMainImg)
					.build();
		}
	
	 public void setAsMainImage() {
		 this.isMainImg = true;
	    }
}
