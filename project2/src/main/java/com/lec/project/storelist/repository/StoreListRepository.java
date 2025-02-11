package com.lec.project.storelist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lec.project.storelist.entity.StoreList;

@Repository
public interface StoreListRepository extends JpaRepository<StoreList, Long> {
	
}