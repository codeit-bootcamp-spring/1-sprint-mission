package com.sprint.mission.discodeit.dto;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReadStatusDto {
    private UUID id;
    private UUID userId;
    private UUID channelId;
    private Instant lastReadAt;
}