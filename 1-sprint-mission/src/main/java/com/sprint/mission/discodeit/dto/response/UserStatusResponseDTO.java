package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@Schema(name = "UserStatusResponseDTO", description = "사용자 상태 응답 정보를 담은 DTO")
public record UserStatusResponseDTO(
    @Schema(description = "상태 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "Status Id is required") UUID statusId,

    @Schema(description = "사용자 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "User Id is required") UUID userId,

    @Schema(description = "마지막 활동 시간", example = "2021-09-01T12:00:00Z")
    Instant lastActiveAt
) {

  public static UserStatusResponseDTO fromEntity(UserStatus userStatus) {
    return new UserStatusResponseDTO(
        userStatus.getId(),
        userStatus.getUser().getId(),
        userStatus.getLastSeenAt()
    );
  }
}
