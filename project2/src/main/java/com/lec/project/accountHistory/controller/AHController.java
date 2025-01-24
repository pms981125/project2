package com.lec.project.accountHistory.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lec.project.account.dto.AccountDTO;
import com.lec.project.accountHistory.dto.AccountHistoryDTO;
import com.lec.project.accountHistory.dto.PageRequestDTO;
import com.lec.project.accountHistory.dto.PageResponseDTO;
import com.lec.project.accountHistory.service.AHService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/accountHistory")
@RequiredArgsConstructor
public class AHController {
	private final AHService ahService;
	
//	@GetMapping("/list")
//	public String getList(PageRequestDTO pageRequestDTO, Model model) {
//		PageResponseDTO<AccountHistoryDTO> responseDTO = ahService.getListOfTransfer(pageRequestDTO);
//		log.info("........" + responseDTO);
//		model.addAttribute("responseDTO", responseDTO);
//		return "/accountHistory/list";
//	}
	
	@GetMapping("/list/{accountId}")
    public ResponseEntity<PageResponseDTO<AccountHistoryDTO>> getList(
            @PathVariable("accountId") Long accountId,
            PageRequestDTO pageRequestDTO) {
        
        try {
            PageResponseDTO<AccountHistoryDTO> responseDTO = ahService.getListOfBoard(accountId, pageRequestDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error in getList: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }
	
//	@GetMapping("/list/{accountId}")
//    public ResponseEntity<List<AccountHistoryDTO>> getTransfers() {
//        List<AccountHistoryDTO> transfers = ahService.getAllTransfers();
//        return ResponseEntity.ok(transfers);
//    }
	
	@PreAuthorize("isAuthenticated")
	@GetMapping({"/read", "/modify"})
	public void read(@RequestParam("accountHistoryId") Long accountHistoryId
					, PageRequestDTO pageRequestDTO, Model model) {
		log.info("read or modify....................");	
		AccountHistoryDTO accountHistoryDTO = ahService.readOne(accountHistoryId);
		model.addAttribute("accountHistoryDTO", accountHistoryDTO);
	}
	
	@GetMapping("/register")
	public void registerGet() {
		log.info("register.GET....................");
	}
	
	@PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void registerPost(@RequestParam("senderAccountId") Long senderAccountId,
            @RequestParam("receiverAccountId") Long receiverAccountId,
            @RequestParam("transferAmount") int transferAmount,
										BindingResult bindingResult,
										Model model) throws BindException {
		
		if(bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}
		
		try {
            ahService.register(senderAccountId, receiverAccountId, transferAmount);
            model.addAttribute("successMessage", "이체가 완료되었습니다!!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
        }
	}

}
