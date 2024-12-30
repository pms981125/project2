package com.lec.project.shoppingmall.service.cart;

import com.lec.project.shoppingmall.dto.PageRequestDTO;
import com.lec.project.shoppingmall.dto.PageResponseDTO;
import com.lec.project.shoppingmall.dto.cart.CartListDTO;

public interface CartService {

	PageResponseDTO<CartListDTO> list(PageRequestDTO pageRequestDTO);
    CartListDTO readOne(Long id);
    void modify(Long id, int count);
    void remove(Long id);
    void removeAll();
    void addToCart(String productCode, int count);
    int getTotalPrice();
	
}
