package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class MessageDto {
    private String id;
    private String channelId;
    private String senderId;
    @NotNull
    private String senderName;
    @NotNull
    private String channelName;
    @NotNull
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}