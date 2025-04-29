package com.sprint.mission.discodeit.dto.message;

import jakarta.validation.constraints.NotNull;

public record MessageUpdateRequest(
    @NotNull(message = "메시지 내용을 입력하세요.")
    String content
) {

}
