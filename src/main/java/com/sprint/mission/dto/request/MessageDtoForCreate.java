package com.sprint.mission.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record MessageDtoForCreate(
        @NotNull(message = "채널 ID는 필수입니다.")
        UUID channelId,
        @NotNull(message = "유저 ID는 필수입니다.")
        UUID userId,
        @NotBlank(message = "내용은 필수입니다.")
        String content) {
}
