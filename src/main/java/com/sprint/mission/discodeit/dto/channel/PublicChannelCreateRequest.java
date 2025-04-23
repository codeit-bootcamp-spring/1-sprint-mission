package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.NotBlank;

public record PublicChannelCreateRequest(
    @NotBlank(message = "채널이름은 필수 입력 값입니다.")
    String name,
    @NotBlank(message = "채널 설명은 필수 입력 값입니다.")
    String description
) {

}
