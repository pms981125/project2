package com.lec.project.shoppingmall.service.product.image;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.lec.project.shoppingmall.domain.product.Product;
import com.lec.project.shoppingmall.domain.product.productimage.ProductImage;
import com.lec.project.shoppingmall.dto.product.image.ProductImageDTO;
import com.lec.project.shoppingmall.repository.ProductImageRepository;
import com.lec.project.shoppingmall.repository.ProductRepository;
import com.lec.project.shoppingmall.util.FileUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class ProductImageServiceImpl implements ProductImageService {

	private final ProductImageRepository productImageRepository;
	private final ProductRepository productRepository;
	private final FileUtils fileUtils;
	private final ModelMapper modelMapper;

	@Override
	public ProductImageDTO uploadProductImage(MultipartFile file, String productCode, boolean isMainImage)
			throws IOException {	

		// 파일 유효성 검사
		if (file == null || file.isEmpty()) {
			throw new IllegalArgumentException("업로드할 파일이 없습니다.........");
		}

		// 이미지 파일 확인
		if (!fileUtils.isImageFile(file) || !fileUtils.isAllowedExtension(file)) {
			throw new IllegalArgumentException("유효하지 않은 이미지 파일입니다.........");
		}

		// 상품 조회
		Product product = productRepository.findById(productCode)
				.orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다.........."));

		// 대표 이미지 초기화
		if (isMainImage) {
			log.info("Resetting main image flag for product: {}", productCode);
			List<ProductImage> existingImages = productImageRepository.findByProductCode(productCode);
			existingImages.forEach(img -> img.setIs_main_img(false));
			productImageRepository.saveAll(existingImages);
		}

		// 원본 이미지 저장
		String originalImagePath = fileUtils.saveOriginalFile(file);

		// 썸네일 생성
		String thumbnailPath = fileUtils.createThumbnail(originalImagePath);

		ProductImage productImage = ProductImage.createProductImage(
				file.getOriginalFilename()
				, originalImagePath
				, originalImagePath
				, thumbnailPath
				, product
				, isMainImage
		);

		ProductImage savedImage = productImageRepository.save(productImage);

		log.info("Image uploaded: {}, Main Image: {}", savedImage.getImg_id(), savedImage.getIs_main_img());

		return modelMapper.map(savedImage, ProductImageDTO.class);
	}

	@Override
	public List<ProductImageDTO> getProductImages(String productCode) {
		List<ProductImage> images = productImageRepository.findByProductCode(productCode);
		log.info("Found {} images for product {}", images.size(), productCode);
		return images.stream().map(image -> modelMapper.map(image, ProductImageDTO.class)).collect(Collectors.toList());
	}

	@Override
	public ProductImageDTO getMainImage(String productCode) {
		return productImageRepository.findMainImageByProductCode(productCode).map(image -> {
			log.info("Main image found for product {}: {}", productCode, image.getImg_id());
			return modelMapper.map(image, ProductImageDTO.class);
		}).orElse(null);
	}

	@Override
	public void deleteImage(String imageId) {
		ProductImage productImage = productImageRepository.findById(imageId)
				.orElseThrow(() -> new IllegalArgumentException("이미지를 찾을 수 없습니다........."));

		try {
			fileUtils.deleteFile(productImage.getImg_path());
			fileUtils.deleteFile(productImage.getThumbnail_path());

			productImageRepository.delete(productImage);

			log.info("Image deleted: {}", imageId);
		} catch (IOException e) {
			log.error("이미지 삭제 중 오류 발생", e);
			throw new RuntimeException("이미지 삭제 중 오류가 발생했습니다..........", e);
		}

	}

	@Override
	public void changeMainImage(String productCode, String imageId) {
		Product product = productRepository.findById(productCode)
				.orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다.........."));

		List<ProductImage> productImages = productImageRepository.findByProductCode(productCode);
		productImages.forEach(img -> img.setIs_main_img(false));
		productImageRepository.saveAll(productImages);
		// 새 대표 이미지 설정
		ProductImage newMainImage = productImageRepository.findById(imageId)
				.orElseThrow(() -> new IllegalArgumentException("이미지를 찾을 수 없습니다."));

		newMainImage.setIs_main_img(true);
		productImageRepository.save(newMainImage);

		log.info("Main image changed for product {}: {}", productCode, imageId);
	}

}
