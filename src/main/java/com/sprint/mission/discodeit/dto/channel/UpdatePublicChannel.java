package com.sprint.mission.discodeit.dto.channel;

import java.util.UUID;

public record UpdatePublicChannel(UUID channelId, String name, String description, UUID newUserID) {
}
