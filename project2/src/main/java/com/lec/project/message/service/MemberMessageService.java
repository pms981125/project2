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

    public MemberMessage getMessageById(Long messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다."));
    }

    public void sendReplyMessage(Long messageId, String senderId, String content) {
        MemberMessage originalMessage = getMessageById(messageId);
        String receiverId = originalMessage.getSender().getId(); // 원본 메시지의 발신자에게 답장

        sendMessage(senderId, receiverId, "Re: " + originalMessage.getContent() + "\n\n" + content);
    }

    public boolean deleteMessage(Long messageId, String currentUserId) {
        MemberMessage message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("메시지가 존재하지 않습니다."));
        
        if (message.getSender().getId().equals(currentUserId) || message.getReceiver().getId().equals(currentUserId)) {
            messageRepository.delete(message);
            return true;
        }
        return false;
    }
}



