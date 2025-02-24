package com.sprint.mission.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChannelDtoForRequest(
        @NotBlank(message = "이름은 필수입니다.")
        @Size(min = 2, max = 10, message = "이름은 2자 이상 10자 이하로 입력해주세요.")
        String newName,
        @NotBlank(message = "설명은 필수입니다.")
        String newDescription) {
}
