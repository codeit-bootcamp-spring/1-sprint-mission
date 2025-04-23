package com.sprint.mission.discodeit.dto.readStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record CreateReadStatusRequest(

    @NotBlank(message = "유저 ID를 입력해주세요.")
    UUID userId,

    @NotBlank(message = "채널 ID를 입력해주세요.")
    UUID channelId,

    @NotNull(message = "마지막으로 메시지를 읽은 시간은 필수입니다.")
    Instant lastReadAt) {

}
