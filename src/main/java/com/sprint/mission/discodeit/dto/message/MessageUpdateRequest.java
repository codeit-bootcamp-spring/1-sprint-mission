package com.sprint.mission.discodeit.dto.message;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record MessageUpdateRequest(
    @NotBlank(message = "message 는 공백일 수 없습니다.")
    String newMessage
) {

}
