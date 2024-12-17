package com.lec.project.shoppingmall.service;

import com.lec.project.shoppingmall.dto.PageRequestDTO;
import com.lec.project.shoppingmall.dto.PageResponseDTO;
import com.lec.project.shoppingmall.dto.ShopDTO;

public interface ShopService {

	Long register(ShopDTO shopDTO);
	ShopDTO readOne(Long sno);
	void modify(ShopDTO shopDTO);
	void remove(Long sno);
	PageResponseDTO<ShopDTO> list(PageRequestDTO pageRequestDTO);
}
