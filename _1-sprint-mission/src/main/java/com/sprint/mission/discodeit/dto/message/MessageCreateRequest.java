package com.sprint.mission.discodeit.dto.message;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Schema(description = "Message 생성 정보")
public record MessageCreateRequest(
    @NotBlank(message = "메시지 내용은 필수입니다.")
    String content,

    @NotNull
    UUID authorId,

    @NotNull
    UUID channelId
) {

}
