package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.NonNull;

public record MessageCreateRequest(
    @NotBlank(message = "메시지를 입력하세요")
    String content,
    @NotNull(message = "채널 id는 필수 입니다")
    UUID channelId,
    @NotNull(message = "저자는 필수 입니다")
    UUID authorId
) {

}
