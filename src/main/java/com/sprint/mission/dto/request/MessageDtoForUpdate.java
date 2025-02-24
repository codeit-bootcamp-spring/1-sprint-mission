package com.sprint.mission.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MessageDtoForUpdate (
        @NotBlank(message = "내용은 필수입니다.")
    String newContent){
}
