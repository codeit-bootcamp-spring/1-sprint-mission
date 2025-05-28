package com.sprint.mission.discodeit.dto.readstatus;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ReadStatusRequestDto(@NotNull UUID userId, @NotNull UUID channelId,
                                   @NotNull Instant lastReadAt) {

}
