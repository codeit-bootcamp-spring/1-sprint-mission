package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record MessageCreateRequest(
    @NotBlank(message = "메세지 내용은 필수입니다.") String content,
    @NotNull(message = "채널 아이디는 필수입니다.") UUID channelId,
    @NotNull(message = "유저 아이디는 필수입니다.") UUID authorId
) {

}
