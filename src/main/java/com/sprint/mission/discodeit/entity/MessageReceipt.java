package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "message_receipt")
public class MessageReceipt implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // ✅ 자동 UUID 생성
    private UUID id;

    @Column(nullable = false)
    private UUID messageId;

    @Column(nullable = false)
    private UUID receiverId;

    @Column(nullable = false)
    private UUID channelId;

    @Column(nullable = false)
    private Instant receivedAt; // ✅ `readAt` → `receivedAt` 변경 (의미 명확화)

    public MessageReceipt(UUID messageId, UUID receiverId, UUID channelId) {
        this.messageId = messageId;
        this.receiverId = receiverId;
        this.channelId = channelId;
        this.receivedAt = Instant.now(); // ✅ 기본값 자동 설정
    }
}
