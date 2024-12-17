package com.lec.project.human_resources.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lec.project.human_resources.domain.Admin;

public interface AdminRepository extends JpaRepository<Admin, String> {
	
}