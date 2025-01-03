package com.lec.project.message.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lec.project.Member;
import com.lec.project.MemberRepository;
import com.lec.project.message.model.MemberMessage;
import com.lec.project.message.repository.MemberMessageRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class MemberMessageService {
    private final MemberMessageRepository messageRepository;
    private final MemberRepository memberRepository;

    public MemberMessageService(MemberMessageRepository messageRepository, MemberRepository memberRepository) {
        this.messageRepository = messageRepository;
        this.memberRepository = memberRepository;
    }

    public void sendMessage(String senderId, String receiverId, String content) {
        Member sender = memberRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("발신자 정보를 찾을 수 없습니다."));
        Member receiver = memberRepository.findById(receiverId)
                .orElseThrow(() -> new IllegalArgumentException("수신자 정보를 찾을 수 없습니다."));

        MemberMessage message = MemberMessage.builder()
                .sender(sender)
                .receiver(receiver)
                .content(content)
                .sentAt(LocalDateTime.now())
                .build();

        messageRepository.save(message);
    }

    public List<MemberMessage> getReceivedMessages(String receiverId) {
        return messageRepository.findByReceiver_Id(receiverId);
    }

    public List<MemberMessage> getSentMessages(String senderId) {
        return messageRepository.findBySender_Id(senderId);
    }
    
    public boolean deleteMessage(Long messageId, String currentUserId) {
        MemberMessage message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("메시지가 존재하지 않습니다."));
        
        // 삭제 권한 확인: 메시지의 발신자 또는 수신자와 현재 사용자가 동일한지 확인
        if (message.getSender().getId().equals(currentUserId) || message.getReceiver().getId().equals(currentUserId)) {
            messageRepository.delete(message);
            return true;
        }
        return false; // 권한 없음
    }

}


