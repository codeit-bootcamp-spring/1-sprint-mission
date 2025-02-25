package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "사용자 상태 업데이트 요청 정보")
public record UserStatusUpdateRequest(
    @Schema(description = "마지막 활동 시간", example = "2025-02-24T12:34:56Z")
    Instant newLastActiveAt
) {

}
