package com.sprint.mission.discodeit.dto.readstatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@Schema(description = "수정할 읽음 상태 정보")
public record ReadStatusUpdateRequest(
    @NotNull
    Instant newLastReadAt
) {

}
