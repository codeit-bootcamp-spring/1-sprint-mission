package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(description = "사용자 상태 생성 요청 정보")
public record UserStatusCreateRequest(

    @Schema(description = "사용자 ID", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID userId,

    @Schema(description = "마지막 활동 시간", example = "2025-02-24T12:34:56Z")
    Instant lastActiveAt
) {

}
