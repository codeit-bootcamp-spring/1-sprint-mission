package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(description = "Private Channel 생성 정보")
public record PrivateChannelCreateRequest(
    @Schema(description = "참여자 ID 목록", example = "[\"550e8400-e29b-41d4-a716-446655440000\"]")
    List<UUID> participantIds
) {

}