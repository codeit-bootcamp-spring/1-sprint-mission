package com.sprint.mission.discodeit.dto.readstatus;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateReadStatusRequestDto {

    @NotNull
    Instant lastReadAt;
    @NotNull
    private UUID userId;
    @NotNull
    private UUID channelId;
}
