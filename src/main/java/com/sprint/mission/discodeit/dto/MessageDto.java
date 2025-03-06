package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter @Builder
@AllArgsConstructor
public class MessageDto {
    private UUID id;
    private UUID channelId;
    private UUID senderId;
    @NotNull
    private String senderName;
    @NotNull
    private String channelName;
    @NotNull
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}