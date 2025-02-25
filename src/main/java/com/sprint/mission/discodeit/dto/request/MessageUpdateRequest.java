package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "수정할 Message 내용")
public record MessageUpdateRequest(
    @Schema(description = "새로운 메시지 내용", example = "업데이트된 안녕하세요!")
    String newContent
) {

}