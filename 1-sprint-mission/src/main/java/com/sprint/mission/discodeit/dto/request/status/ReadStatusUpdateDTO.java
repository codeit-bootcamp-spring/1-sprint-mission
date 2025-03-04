package com.sprint.mission.discodeit.dto.request.status;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@Schema(name = "ReadStatusUpdateDTO", description = "읽음 상태 업데이트 요청 정보를 담은 DTO")
public record ReadStatusUpdateDTO(
    @Schema(description = "읽음 상태 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull UUID readStatusId,

    @Schema(description = "마지막 읽은 시간", example = "2021-09-01T12:00:00Z")
    @NotEmpty Instant lastReadAt
) {

}
