package com.lec.project.shoppingmall.service.cart;

import com.lec.project.shoppingmall.dto.PageRequestDTO;
import com.lec.project.shoppingmall.dto.PageResponseDTO;
import com.lec.project.shoppingmall.dto.cart.CartListDTO;

public interface CartService {

	PageResponseDTO<CartListDTO> list(PageRequestDTO pageRequestDTO, String memberid);
    CartListDTO readOne(Long id, String memberid);
    void modify(Long id, int count, String memberid);
    void remove(Long id, String memberid);
    void removeAll(String memberid);
    void addToCart(String memberid, String productCode, int count);
    int getTotalPrice(String memberid);
	
}
