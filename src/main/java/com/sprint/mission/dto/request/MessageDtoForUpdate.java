package com.sprint.mission.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record MessageDtoForUpdate (
        @NotBlank(message = "내용은 필수입니다.")
        @Schema(example = "오늘은 코딩하고 싶습니다.")
    String newContent){
}
