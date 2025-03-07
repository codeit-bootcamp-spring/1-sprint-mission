package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity @Builder
@Getter @Setter
@Table(name = "messages")
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue
    @Column(name = "message_id")
    private UUID id;

    @Column(name = "channel_id")
    private UUID channelId;

    @Column(name = "sender_id")
    private UUID senderId;

    @Column(name = "sender_name")
    private String senderName;

    @Column(name = "channel_name")
    private String ChannelName;

    @Column(nullable = false)
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}