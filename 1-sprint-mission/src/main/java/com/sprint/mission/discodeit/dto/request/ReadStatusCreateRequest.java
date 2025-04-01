package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
    @NotNull(message = "유저 아이디는 필수입니다.")
    UUID userId,

    @NotNull(message = "채널 아이디는 필수입니다.")
    UUID channelId,
    Instant lastReadAt
) {

}
