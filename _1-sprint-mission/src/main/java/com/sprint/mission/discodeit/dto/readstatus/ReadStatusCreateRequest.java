package com.sprint.mission.discodeit.dto.readstatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@Schema(description = "Message 읽음 상태 생성 정보")
public record ReadStatusCreateRequest(
    @NotNull
    UUID userId,
    @NotNull
    UUID channelId,
    @NotNull
    Instant lastReadAt
) {

}
