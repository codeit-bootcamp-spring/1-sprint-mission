package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequest(
    @NotNull
    List<UUID> participantIds
) {

}
