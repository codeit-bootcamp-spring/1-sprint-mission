package com.sprint.mission.discodeit.dto.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadStatusDto {
    UUID id;
    UUID userId;
    UUID channelId;
    Instant lastReadAt; // TODO찐: date-time으로 수정
}