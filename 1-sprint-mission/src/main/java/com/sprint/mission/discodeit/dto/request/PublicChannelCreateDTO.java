package com.sprint.mission.discodeit.dto.request;

import java.util.UUID;

public record PublicChannelCreateDTO(UUID ownerId, String name, String description) {
}
