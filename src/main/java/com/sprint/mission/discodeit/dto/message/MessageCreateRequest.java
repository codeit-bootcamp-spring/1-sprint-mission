package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.Channel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record MessageCreateRequest(
    @NotNull(message = "channelId 필드는 반드시 값이 필요합니다.")
    UUID channelId,
    @NotNull(message = "authorId 필드는 반드시 값이 필요합니다.")
    UUID authorId,
    @NotBlank(message = "message 는 공백일 수 없습니다.")
    String content
) {

}
