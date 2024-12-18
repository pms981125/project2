package com.lec.project.shoppingmall.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lec.project.shoppingmall.domain.Shop;
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
	
	private final ModelMapper modelMapper;
	private final ShopRepository shopRepository;

	@Override
	public PageResponseDTO<ShopDTO> list(PageRequestDTO pageRequestDTO) {
		
		String keyword = pageRequestDTO.getKeyword();
		Pageable pageable = pageRequestDTO.getPageable("sno");
		
		Page<Shop> result = shopRepository.searchAllImpl(keyword, pageable);
		List<ShopDTO> dtoList = result.getContent()
									.stream()
									.map(shop -> modelMapper.map(shop, ShopDTO.class))
									.collect(Collectors.toList());
		
		return PageResponseDTO.<ShopDTO>withAll()
				.pageRequestDTO(pageRequestDTO)
				.dtoList(dtoList)
				.total((int)result.getTotalElements())
				.build();
	}
	
	@Override
	public Long register(ShopDTO shopDTO) {
		Shop shop = modelMapper.map(shopDTO, Shop.class);
		Long sno = shopRepository.save(shop).getSno();
		return sno;
	}

	@Override
	public ShopDTO readOne(Long sno) {
		Optional<Shop> result = shopRepository.findById(sno);
		Shop shop = result.orElseThrow();
		ShopDTO shopDTO = modelMapper.map(shop, ShopDTO.class);
		return shopDTO;
	}

	@Override
	public void modify(ShopDTO shopDTO) {

		Optional<Shop> result = shopRepository.findById(shopDTO.getSno());
		Shop shop = result.orElseThrow();
		shop.changePrice(shopDTO.getGPrice());
		
	}

	@Override
	public void remove(Long sno) {
		shopRepository.deleteById(sno);
		
	}


}
