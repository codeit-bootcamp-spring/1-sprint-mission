package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.ReadStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@Schema(name = "ReadStatusResponseDTO", description = "읽음 상태 응답 정보를 담은 DTO")
public record ReadStatusResponseDTO(
    @Schema(description = "읽음 상태 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "ReadStatus Id is required") UUID readStatusId,

    @Schema(description = "사용자 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "User Id is required") UUID userId,

    @Schema(description = "채널 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "Channel Id is required") UUID channelId,

    @Schema(description = "마지막 읽은 시간", example = "2021-09-01T12:00:00Z")
    Instant lastReadAt
) {

  public static ReadStatusResponseDTO fromEntity(ReadStatus readStatus) {
    return new ReadStatusResponseDTO(
        readStatus.getId(),
        readStatus.getUser().getId(),
        readStatus.getChannel().getId(),
        readStatus.getLastReadAt()
    );
  }

}
