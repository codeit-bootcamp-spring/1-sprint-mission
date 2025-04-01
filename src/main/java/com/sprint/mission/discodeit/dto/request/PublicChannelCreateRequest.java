package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PublicChannelCreateRequest(
        @NotBlank(message = "채널 이름은 필수입니다.")
        String name,

        @NotBlank(message = "채널 설명은 필수입니다.")
        String description
) {
}
