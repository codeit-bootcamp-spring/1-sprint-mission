package com.sprint.mission.dto.request;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateDTO(
    List<UUID> participantIds) {

}
