package com.lec.project.shoppingmall.service.product.image;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.lec.project.shoppingmall.dto.product.image.ProductImageDTO;

public interface ProductImageService {
	
	ProductImageDTO uploadProductImage(
			MultipartFile file
			, String productCode
			, boolean isMainImage
			) throws IOException;
	
	List<ProductImageDTO> getProductImages(String productCode);
	
	ProductImageDTO getMainImage(String productCode);
	
	void deleteImage(String imageId);
	
	void changeMainImage(String productCode, String imageId);

	//여러 이미지 추가
	List<ProductImageDTO> uploadMultipleProductImages(
			List<MultipartFile> files
			, String productCode
			, boolean isMainImage
			) throws IOException;
	
	
}
