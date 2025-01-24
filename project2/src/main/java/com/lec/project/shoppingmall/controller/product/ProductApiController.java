package com.lec.project.shoppingmall.controller.product;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lec.project.shoppingmall.dto.product.ProductDTO;
import com.lec.project.shoppingmall.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Log4j2
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductApiController {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    
    @GetMapping("/{productCode}")
    public ResponseEntity<?> getProductByCode(
    		@PathVariable("productCode") String productCode
    		, @RequestParam(value = "paramName", required = false, defaultValue = "default") String someParameter) {
        log.info("product Search Request: " + productCode);
        
        return productRepository.findById(productCode)
                .map(product -> {
                    ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
                    return ResponseEntity.ok(productDTO);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}