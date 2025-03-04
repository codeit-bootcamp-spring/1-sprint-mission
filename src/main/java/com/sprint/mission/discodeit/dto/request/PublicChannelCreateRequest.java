package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Public Channel 생성 정보")
public record PublicChannelCreateRequest(
    @Schema(description = "채널 이름", example = "General")
    String name,

    @Schema(description = "채널 설명", example = "모두를 위한 일반 채널")
    String description
) {

}