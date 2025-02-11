package com.lec.project.storelist.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.lec.project.storelist.entity.StoreList;
import com.lec.project.storelist.service.StoreListService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/stores")
public class StoreListController {
    private final StoreListService storeService;
    
    //매장 목록 조회
    @GetMapping
    public String storeList(Model model) {
        List<StoreList> stores = storeService.getAllStores();
        model.addAttribute("stores", stores);
        return "storelist/list";
    }
    
    //매장 상세 조회
    @GetMapping("/{id}")
    public String storeDetail(@PathVariable("id") Long id, Model model) {
        StoreList store = storeService.getStoreById(id);
        model.addAttribute("store", store);
        return "storelist/detail";
    }    

    // 매장 삭제 (Delete)
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping("/delete/{id}")
    public String deleteStore(@PathVariable("id") Long id) {
        storeService.deleteStore(id);
        return "redirect:/stores";
    }
    
 // 매장 생성 폼 페이지 이동
    @GetMapping("/new")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public String createStoreForm(Model model) {
        model.addAttribute("store", new StoreList()); // 빈 객체 전달
        return "storelist/storecreate"; // storecreate.html로 이동
    }

    // 매장 생성 처리
    @PostMapping("/new")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public String createStore(@ModelAttribute StoreList store) {
        storeService.createStore(store);
        return "redirect:/stores"; // 매장 목록으로 이동
    }
    
    // 매장 수정 폼 페이지 이동
    @GetMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public String updateStoreForm(@PathVariable("id") Long id, Model model) {
        StoreList store = storeService.getStoreById(id); // 수정할 매장 정보 가져오기
        model.addAttribute("store", store); // 폼에 전달할 매장 정보
        return "storelist/storeupdate"; // storeupdate.html로 이동
    }

    // 매장 수정 처리
    @PostMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public String updateStore(@PathVariable("id") Long id, @ModelAttribute StoreList store) {
        store.setStoreId(id); // 전달된 매장 ID 설정
        storeService.updateStore(store); // 서비스에서 업데이트 처리
        return "redirect:/stores"; // 매장 목록으로 이동
    }

}

