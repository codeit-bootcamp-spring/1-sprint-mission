package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Public 채널 정보 수정 DTO")
public record PublicChannelUpdateRequest(
    String newName,
    String newDescription
) {

}
