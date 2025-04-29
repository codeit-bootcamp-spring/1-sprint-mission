package com.sprint.mission.discodeit.dto.userstatus;

import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserStatusRequest
    (
        @NotBlank(message = "유저 ID는 필수입니다.") UUID userId,
        Instant lastAccessedAt
    ) {

}