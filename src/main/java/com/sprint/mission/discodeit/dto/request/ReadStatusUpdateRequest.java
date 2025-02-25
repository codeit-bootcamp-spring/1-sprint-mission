package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "수정할 읽음 상태 정보")
public record ReadStatusUpdateRequest(
    @Schema(description = "새로운 마지막 읽은 시간", example = "2025-02-24T12:00:00Z")
    Instant newLastReadAt
) {

}