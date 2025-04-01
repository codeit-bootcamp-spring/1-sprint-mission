package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PublicChannelUpdateRequest(
        @NotBlank(message = "새 이름은 필수입니다.")
        String newName,

        @NotBlank(message = "새 설명은 필수입니다.")
        String newDescription
) {
}
