package com.sprint.mission.discodeit.dto;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter @Builder
@AllArgsConstructor
public class MessageDto {
    private UUID id;
    private UUID channelId;
    private UUID authorId;
    private String author;
    private String channelName;
    private String content;
    private Instant createdAt;
    private Instant updatedAt;
}