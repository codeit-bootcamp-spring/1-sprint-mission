package com.sprint.mission.discodeit.dto.userStatusDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record CreateUserStatusRequest(

    @NotBlank(message = "유저 ID를 입력해주세요.")
    UUID userId,

    @NotNull(message = "마지막 접속 시간은 필수입니다.")
    Instant lastActiveAt
) {

}
