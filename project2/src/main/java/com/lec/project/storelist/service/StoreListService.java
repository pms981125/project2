package com.lec.project.storelist.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.lec.project.storelist.entity.StoreList;
import com.lec.project.storelist.repository.StoreListRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreListService {
    private final StoreListRepository storeRepository;
    
    // 모든 매장 조회
    public List<StoreList> getAllStores() {
        return storeRepository.findAll();
    }
    
    // 특정 매장 조회
    public StoreList getStoreById(Long id) {
        return storeRepository.findById(id).orElse(null);
    }
    
    // 매장 삭제
    public void deleteStore(Long id) {
        storeRepository.deleteById(id);
    }
    
    // 매장 생성
    public void createStore(StoreList store) {
        storeRepository.save(store);
    }
    
    // 매장 수정
    public void updateStore(StoreList store) {
        storeRepository.save(store); // ID가 존재하면 기존 데이터를 업데이트
    }
}