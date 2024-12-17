package com.lec.project.human_resources.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.lec.project.human_resources.domain.Admin;
import com.lec.project.human_resources.dto.AdminDTO;
import com.lec.project.human_resources.repository.AdminRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class HRServiceImpl implements HRService {
	private final AdminRepository adminRepository;
	private final ModelMapper modelMapper;
	
	@Override
	public AdminDTO getAdmin(String id) {
		Optional<Admin> result = adminRepository.findById(id);
		Admin admin = result.orElseThrow();
		AdminDTO adminDTO = modelMapper.map(admin, AdminDTO.class);
		
		return adminDTO;
	}
}