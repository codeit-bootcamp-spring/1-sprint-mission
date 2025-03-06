package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Entity
@Getter @Setter
@Table(name = "messages")
@RequiredArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "channel_id")
    private String channelId;

    @Column(name = "sender_id")
    private String senderId;

    @Column(name = "sender_name")
    private String senderName;

    @Column(name = "channel_name")
    private String ChannelName;

    @Column(nullable = false)
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}