package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MessageUpdateRequest(
    @NotBlank(message = "메시지 내용은 필수입니다")
    @Size(max = 2000, message = "메시지 내용은 2000자 이하여야 합니다")
    String newContent
) {

}
