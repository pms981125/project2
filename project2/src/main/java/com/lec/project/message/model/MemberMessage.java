package com.lec.project.message.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.lec.project.Member;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 메시지 ID

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Member sender; // 발신자

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Member receiver; // 수신자

    private String content; // 메시지 내용
    private LocalDateTime sentAt; // 보낸 시간
}