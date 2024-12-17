package com.lec.project.human_resources.service;

import com.lec.project.human_resources.dto.AdminDTO;

public interface HRService {
	AdminDTO getAdmin(String id);
}