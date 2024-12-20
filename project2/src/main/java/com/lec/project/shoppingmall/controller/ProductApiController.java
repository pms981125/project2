package com.lec.project.shoppingmall.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import com.lec.project.shoppingmall.domain.product.Product;
import com.lec.project.shoppingmall.service.product.ProductService;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
@Log4j2
public class ProductApiController {
   
   private final ProductService productService;
   
   @GetMapping("/{productCode}")
   public ResponseEntity<Product> getProduct(@PathVariable String productCode) {
       try {
           Product product = productService.findById(productCode);
           return ResponseEntity.ok(product);
       } catch (Exception e) {
           log.error("상품 조회 실패 - productCode: " + productCode, e);
           return ResponseEntity.notFound().build();
       }
   }
}