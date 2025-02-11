package com.lec.project.shoppingmall.service.Stock;

import com.lec.project.shoppingmall.domain.product.Product;
import com.lec.project.shoppingmall.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockService {

    private final ProductRepository productRepository;

    @Autowired
    public StockService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // 모든 상품을 가져오는 메서드 (인자 없음)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // 상품 코드로 특정 상품을 검색하는 메서드
    public Optional<Product> getProductByCode(String productCode) {
        return productRepository.findById(productCode);
    }
    
    // 상품 코드로 부분 검색 (Containing)
    public List<Product> getProductsByCode(String productCode) {
        return productRepository.findByProductCodeContaining(productCode);
    }

    // 상품 재고 업데이트
    public void updateStock(String productCode, int updatedStock) {
        Optional<Product> productOptional = productRepository.findById(productCode);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setProductStock(updatedStock);
            productRepository.save(product);
        }
    }
}
