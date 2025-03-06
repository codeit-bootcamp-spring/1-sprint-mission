package com.sprint.mission.discodeit.dto.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;


@Schema(name = "UserStatusUpdateDTO", description = "사용자 상태 업데이트 요청 정보를 담은 DTO")
public record UserStatusUpdateDTO(
    Instant newLastActiveAt
) {

}
