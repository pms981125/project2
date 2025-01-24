package com.lec.project.account.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lec.project.account.service.AccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountApiController {
    private final AccountService accountService;
//test
    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkUserHasAccount() {
        boolean hasAccount = accountService.hasAccountForCurrentUser();
        return ResponseEntity.ok(Map.of("hasAccount", hasAccount));
    }
    
    @GetMapping("/check-manager")
    public ResponseEntity<Map<String, Boolean>> checkManagerHasAccount() {
    	boolean hasAccount = accountService.hasManagerAccount();
    	return ResponseEntity.ok(Map.of("hasAccount", hasAccount));
    }
}