package com.lec.project.message.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lec.project.message.model.MemberMessage;

import java.util.List;

@Repository
public interface MemberMessageRepository extends JpaRepository<MemberMessage, Long> {
    List<MemberMessage> findByReceiver_Id(String receiverId); // 받은 메시지
    List<MemberMessage> findBySender_Id(String senderId); // 보낸 메시지
}