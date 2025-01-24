package com.lec.project.account.controller;

import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lec.project.account.dto.AccountDTO;
import com.lec.project.account.dto.PageRequestDTO;
import com.lec.project.account.dto.PageResponseDTO;
import com.lec.project.account.service.AccountService;
import com.lec.project.accountHistory.dto.AccountHistoryDTO;
import com.lec.project.accountHistory.service.AHService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
	private final AccountService accountService;
	private final AHService ahService;

	@GetMapping("/list")
	public void list(PageRequestDTO pageRequestDTO, Model model) {
		PageResponseDTO<AccountDTO> responseDTO = accountService.list(pageRequestDTO);
		log.info("........" + responseDTO);
		model.addAttribute("responseDTO", responseDTO);
	}

	@PreAuthorize("isAuthenticated")
	@GetMapping({ "/read", "/modify" })
	public void read(@RequestParam("accountId") Long accountId, PageRequestDTO pageRequestDTO, Model model) {
		log.info("read or modify....................");
		AccountDTO accountDTO = accountService.readOne(accountId);
		model.addAttribute("accountDTO", accountDTO);
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/register")
	public void registerGet() {
		log.info("register.GET....................");
	}

	@GetMapping("/checkAccountId")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> checkAccountId(@RequestParam("accountId") Long accountId) {
        boolean isDuplicate = accountService.isAccountIdExists(accountId);
        return ResponseEntity.ok(Map.of("isDuplicate", isDuplicate));
    }

    @PostMapping("/register")
    public String registerPost(AccountDTO accountDTO, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        log.info("register.Post....................");

        if (bindingResult.hasErrors()) {
            log.info("입력된 정보에 에러가 있습니다..........");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/account/register";
        }

        // Check for duplicate account ID
        if (accountService.isAccountIdExists(accountDTO.getAccountId())) {
            redirectAttributes.addFlashAttribute("error", "이미 존재하는 계좌번호입니다.");
            return "redirect:/account/register";
        }

        log.info("register......... " + accountDTO);

        Long accountId = accountService.register(accountDTO);
        redirectAttributes.addFlashAttribute("result", accountId);

        return "redirect:/account/list";
    }

	@GetMapping("/transfer")
	public void transferGet() {

	}
///////////////////////////////////////////원래 주석///////////////////////////////////////////////////////////////////
	/* 원본 코드
	 * @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
	 * public ResponseEntity<?> addTransfer(@RequestBody Map<String, Object> transferObj) {
	 */
	@PostMapping(value = "/transfer", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addTransfer(@RequestBody Map<String, Object> transferObj) {
		Long senderAccountId;
		Long receiverAccountId;
		Integer transferAmount;

		try {
			senderAccountId = Long.valueOf(transferObj.get("senderAccountId").toString());
			receiverAccountId = Long.valueOf(transferObj.get("receiverAccountId").toString());
			transferAmount = Integer.valueOf(transferObj.get("transferAmount").toString());
		} catch (Exception e) {
			return ResponseEntity.badRequest()
					.body(Map.of("success", false, "message", "Invalid input data: " + e.getMessage()));
		}

		try {
			accountService.transfer(senderAccountId, receiverAccountId, transferAmount);

			return ResponseEntity.ok(Map.of("success", true, "message", "Transfer completed successfully!"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("success", false, "message", "Transfer failed: " + e.getMessage()));
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete")
	public String deleteAccount(@RequestParam("accountId") Long accountId, RedirectAttributes redirectAttributes) {
	    try {
	        accountService.deleteAccount(accountId);
	        redirectAttributes.addFlashAttribute("msg", "계좌가 삭제되었습니다.");
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("error", e.getMessage());
	    }
	    return "redirect:/account/list";
	}
	
//	@PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<?> addDeposit(@RequestBody Map<String, Object> depositObj) {
	@PostMapping(value = "/deposit", consumes = MediaType.APPLICATION_JSON_VALUE) 
	public ResponseEntity<?> addDeposit(@RequestBody Map<String, Object> depositObj) {
	    Long accountId;
	    Integer depositAmount;

	    try {
	        accountId = Long.valueOf(depositObj.get("accountId").toString());
	        depositAmount = Integer.valueOf(depositObj.get("depositAmount").toString());
	    } catch (Exception e) {
	        return ResponseEntity.badRequest()
	                .body(Map.of("success", false, "message", "잘못된 입력값입니다."));
	    }

	    try {
	        accountService.deposit(accountId, depositAmount);
	        return ResponseEntity.ok(Map.of("success", true, "message", "입금이 완료되었습니다."));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Map.of("success", false, "message", e.getMessage()));
	    }
	}
}