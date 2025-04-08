package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequest(
        @NotNull(message = "Participant ids cannot null.")
        List<UUID> participantIds
) {

}
