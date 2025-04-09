package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MessageUpdateRequest(
        @NotBlank(message = "변경할 메시지 내용은 필수입니다.")
        String newContent
) {
}
