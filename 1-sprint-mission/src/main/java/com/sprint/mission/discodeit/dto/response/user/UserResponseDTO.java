package com.sprint.mission.discodeit.dto.response.user;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Schema(name = "UserResponseDTO", description = "사용자 응답 정보를 담은 DTO")
public record UserResponseDTO(
    @Schema(description = "사용자 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "User Id is required") UUID userId,

    @Schema(description = "사용자 이름", example = "john_doe")
    @NotEmpty(message = "Username is required") String username,

    @Schema(description = "사용자 이메일", example = "john@example.com")
    @NotEmpty(message = "Email is required") String email,

    @Schema(description = "생성 시간", example = "2021-09-01T12:00:00Z")
    Instant createdAt,

    @Schema(description = "업데이트 시간", example = "2021-09-01T12:00:00Z")
    Instant updatedAt,

    @Schema(description = "온라인 상태 여부", example = "true")
    boolean isOnline,

    @Schema(description = "프로필 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID profileId
) {

  public static UserResponseDTO fromEntity(User user, UserStatus userStatus,
      Optional<BinaryContent> profileContentOpt) {
    boolean isOnline = userStatus != null && userStatus.isOnline();
    UUID profileId = profileContentOpt.map(BinaryContent::getId).orElse(null);
    return new UserResponseDTO(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        isOnline,
        profileId
    );
  }

}
