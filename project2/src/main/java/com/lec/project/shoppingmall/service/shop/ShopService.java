package com.lec.project.shoppingmall.service.shop;

import com.lec.project.shoppingmall.dto.PageRequestDTO;
import com.lec.project.shoppingmall.dto.PageResponseDTO;
import com.lec.project.shoppingmall.dto.shop.ShopDTO;

public interface ShopService {

	Long register(ShopDTO shopDTO);
	ShopDTO readOne(Long bno);
	ShopDTO readByProductCode(String productCode);
	void modify(ShopDTO shopDTO);
	void remove(Long bno);
	PageResponseDTO<ShopDTO> list(PageRequestDTO pageRequestDTO, String keyword, String category);
}
