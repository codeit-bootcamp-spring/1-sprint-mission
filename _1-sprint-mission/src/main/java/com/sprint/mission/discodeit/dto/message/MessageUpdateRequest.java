package com.sprint.mission.discodeit.dto.message;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "수정할 Message 내용")
public record MessageUpdateRequest(

    @NotBlank(message = "메시지 내용은 필수입니다.")
    String newContent
) {

}
