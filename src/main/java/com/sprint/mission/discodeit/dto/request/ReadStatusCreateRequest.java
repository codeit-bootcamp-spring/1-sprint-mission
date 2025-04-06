package com.sprint.mission.discodeit.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class ReadStatusCreateRequest {
    UUID userId;
    UUID channelId;
    Instant lastReadAt; // TODO찐 : date-time으로 수정
}