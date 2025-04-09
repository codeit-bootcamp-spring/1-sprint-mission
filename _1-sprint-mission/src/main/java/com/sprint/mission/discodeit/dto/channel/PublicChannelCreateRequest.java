package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PublicChannelCreateRequest(
    @NotBlank(message = "채널 이름은 필수입니다.")
    String name,

    @NotBlank(message = "채널 설명은 필수입니다.")
    @Size(max = 100, message = "최대 100자까지 가능합니다.")
    String description
) {

}
