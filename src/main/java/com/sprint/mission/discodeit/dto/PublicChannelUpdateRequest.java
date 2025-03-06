package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record PublicChannelUpdateRequest(
    UUID adminId,
    @NotBlank(message = "채널이름은 필수 입력 값입니다.")
    String newName,
    String newDescription
) {

}
