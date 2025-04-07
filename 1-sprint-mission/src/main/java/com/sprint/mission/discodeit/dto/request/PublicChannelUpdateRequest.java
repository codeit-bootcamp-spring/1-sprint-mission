package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PublicChannelUpdateRequest(
    @NotBlank(message = "새로운 채널 이름은 필수입니다.") String newName,
    @NotBlank(message = "새로운 채널 설명은 필수입니다.") String newDescription
) {

}
