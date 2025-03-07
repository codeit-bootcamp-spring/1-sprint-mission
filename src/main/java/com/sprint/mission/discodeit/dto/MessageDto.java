package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter @Builder
@AllArgsConstructor
public class MessageDto {
    private UUID id;
    private UUID channelId;
    private UUID AuthorId;
    @NotNull
    private String senderName;
    @NotNull
    private String channelName;
    @NotNull
    private String content;
    private Instant createdAt;
    private Instant updatedAt;
}