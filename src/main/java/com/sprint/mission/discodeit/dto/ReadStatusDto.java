package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReadStatusDto {
    private String id;
    private String userId;
    private String channelId;
    private Instant lastReadTime;
}