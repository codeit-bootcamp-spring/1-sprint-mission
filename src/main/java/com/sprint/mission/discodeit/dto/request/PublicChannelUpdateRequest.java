package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "수정할 Channel 정보")
public record PublicChannelUpdateRequest(
    @Schema(description = "새로운 채널 이름", example = "Updated General")
    String newName,

    @Schema(description = "새로운 채널 설명", example = "업데이트된 일반 채널")
    String newDescription
) {

}