package com.lec.project.message.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lec.project.message.model.MemberMessage;
import com.lec.project.message.service.MemberMessageService;

import jakarta.servlet.http.HttpServletRequest;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/messages")
public class MemberMessageController {
    private final MemberMessageService messageService;

    public MemberMessageController(MemberMessageService messageService) {
        this.messageService = messageService;
    }

    // 메시지함 (받은 메시지 + 보낸 메시지 통합)
    @GetMapping("/inbox")
    public String getMessageInbox(Model model, Principal principal) {
        String userId = principal.getName(); // 로그인된 사용자 ID
        
        // 받은 메시지
        List<MemberMessage> receivedMessages = messageService.getReceivedMessages(userId);
        
        // 보낸 메시지
        List<MemberMessage> sentMessages = messageService.getSentMessages(userId);

        model.addAttribute("receivedMessages", receivedMessages);
        model.addAttribute("sentMessages", sentMessages);
        return "Message/messageInbox"; // 메시지함 페이지
    }

    // 메시지 작성 폼
    @GetMapping("/send")
    public String sendMessageForm(Model model, HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        model.addAttribute("_csrf", csrfToken);
        return "Message/sendMessage";
    }
    
    // 메시지 전송
    @PostMapping("/send")
    public String sendMessage(@RequestParam("receiverId") String receiverId, 
                              @RequestParam("content") String content, 
                              Principal principal) {
        String senderId = principal.getName();
        messageService.sendMessage(senderId, receiverId, content);
        return "redirect:/messages/inbox";
    }
    
    // 답장 페이지
    @GetMapping("/reply/{id}")
    public String replyMessage(@PathVariable("id") Long messageId, Model model) {
        MemberMessage message = messageService.getMessageById(messageId);
        model.addAttribute("message", message);
        return "Message/replyMessage"; // replyMessage 페이지로 리턴
    }

    // 답장 전송
    @PostMapping("/reply/{id}")
    public String replyMessage(@PathVariable("id") Long messageId, 
                               @RequestParam("content") String content, 
                               Principal principal) {
        String senderId = principal.getName();
        messageService.sendReplyMessage(messageId, senderId, content);
        return "redirect:/messages/inbox"; // 메시지함으로 리디렉션
    }
    
  
    @PostMapping("/delete/{id}")
    public String deleteMessage(@PathVariable("id") Long messageId, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            // 로그인한 사용자 ID
            String userId = principal.getName();
            // 메시지 삭제 서비스 호출
            messageService.deleteMessage(messageId, userId);
            redirectAttributes.addFlashAttribute("success", "메시지가 성공적으로 삭제되었습니다.");
        } catch (SecurityException e) {
            // 삭제 권한이 없는 경우
            redirectAttributes.addFlashAttribute("error", "삭제 권한이 없습니다.");
        } catch (Exception e) {
            // 기타 오류 처리
            redirectAttributes.addFlashAttribute("error", "메시지 삭제 중 오류가 발생했습니다.");
        }
        return "redirect:/messages/inbox"; // 메시지함으로 리디렉션
    }

}






