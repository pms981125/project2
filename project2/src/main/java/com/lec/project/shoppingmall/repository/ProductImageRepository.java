package com.lec.project.shoppingmall.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lec.project.shoppingmall.domain.product.Product;
import com.lec.project.shoppingmall.domain.product.productimage.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, String>{

    List<ProductImage> findByProduct(Product product);
    
    @Query("SELECT pi FROM ProductImage pi WHERE pi.product = :product AND pi.is_main_img = true")
    Optional<ProductImage> findByProductAndMainImgTrue(@Param("product") Product product);
    
    @Query("SELECT pi FROM ProductImage pi WHERE pi.product.product_code = :productCode")
    List<ProductImage> findAllByProductCode(@Param("productCode") String productCode);
    
    @Query("SELECT pi FROM ProductImage pi WHERE pi.product.product_code = :productCode AND pi.is_main_img = true")
    Optional<ProductImage> findMainImageByProductCode(@Param("productCode") String productCode);
    
    @Query("UPDATE ProductImage pi SET pi.is_main_img = false WHERE pi.product = :product")
    void resetMainImageFlag(@Param("product") Product product);
}
