package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(description = "Message 읽음 상태 생성 정보")
public record ReadStatusCreateRequest(
    @Schema(description = "사용자 ID", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID userId,

    @Schema(description = "채널 ID", example = "550e8400-e29b-41d4-a716-446655440001")
    UUID channelId,

    @Schema(description = "마지막 읽은 시간", example = "2025-02-24T10:00:00Z")
    Instant lastReadAt
) {

}