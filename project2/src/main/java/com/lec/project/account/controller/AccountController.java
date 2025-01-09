package com.lec.project.account.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lec.project.account.dto.AccountDTO;
import com.lec.project.account.dto.PageRequestDTO;
import com.lec.project.account.dto.PageResponseDTO;
import com.lec.project.account.service.AccountService;
import com.lec.project.accountHistory.dto.AccountHistoryDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
	private final AccountService accountService;
	//asdasd
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

	@GetMapping("/register")
	public void registerGet() {
		log.info("register.GET....................");
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

		log.info("register......... " + accountDTO);

		Long accountId = accountService.register(accountDTO);
		redirectAttributes.addFlashAttribute("result", accountId);

		return "redirect:/account/list";
	}

	@GetMapping("/transfer")
	public void transferGet() {

	}

	@PostMapping("/transfer")
    public String transfer(
    		@RequestParam("senderAccountId") Long senderAccountId,
            @RequestParam("receiverAccountId") Long receiverAccountId,
            @RequestParam("transferAmount") int transferAmount,
            Model model) {
        try {
            accountService.transfer(senderAccountId, receiverAccountId, transferAmount);
            model.addAttribute("successMessage", "이체가 완료되었습니다!!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/account/list";
    }
	
	
}
