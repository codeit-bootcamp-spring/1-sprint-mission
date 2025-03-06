package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReadStatusDto {
    private UUID id;
    private UUID userId;
    private UUID channelId;
    private Instant lastReadTime;
}