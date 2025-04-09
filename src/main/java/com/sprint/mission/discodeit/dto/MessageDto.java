package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter @Builder
@AllArgsConstructor
public class MessageDto {
    private UUID id;
    
    @NotNull(message = "채널 ID는 필수입니다.")
    private UUID channelId;
    
    private UUID authorId;
    private String author;
    private String channelName;
    
    @NotBlank(message = "메시지 내용은 필수입니다.")
    private String content;
    
    private Instant createdAt;
    private LocalDateTime updatedAt;
}