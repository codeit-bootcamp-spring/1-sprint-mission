package com.sprint.mission.discodeit.dto.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@Schema(name = "UserStatusDTO", description = "사용자 상태 정보를 담은 DTO")
public record UserStatusDTO(

    @Schema(description = "사용자 식별자", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "UserId is required") UUID userId,

    @Schema(description = "마지막 확인 시간", example = "2021-09-01T12:00:00Z")
    Instant lastSeenAt,

    @Schema(description = "온라인 여부", example = "true")
    boolean isOnline
) {

}
