package com.lec.project.shoppingmall.service;

import java.awt.print.Pageable;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.lec.project.shoppingmall.dto.PageRequestDTO;
import com.lec.project.shoppingmall.dto.PageResponseDTO;
import com.lec.project.shoppingmall.dto.ShopDTO;
import com.lec.project.shoppingmall.repository.ShopRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class ShopServiceImpl implements ShopService{
	//test
	private final ModelMapper modelMapper;
	private final ShopRepository shopRepository;

	@Override
	public PageResponseDTO<ShopDTO> list(PageRequestDTO pageRequestDTO) {
		
//		String keyword = pageRequestDTO.getKeyword();
//		Pageable pageable = pageRequestDTO.getPageable("sno");
//		
//		Page<Shop> result = shopRepository.searchAllImpl(keyword,)
		
		return null;
	}
	
	@Override
	public Long register(ShopDTO shopDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ShopDTO readOne(Long sno) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void modify(ShopDTO shopDTO) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(Long sno) {
		// TODO Auto-generated method stub
		
	}


}
