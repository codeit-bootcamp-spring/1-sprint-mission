package com.sprint.mission.discodeit.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class MessageDTO {
    private String id;
    private String channelId;
    private String senderId;
    private String senderName;
    private String channelName;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}