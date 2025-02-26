package com.sprint.mission.discodeit.dto.request;

import java.util.UUID;

public record ReadStatusRequest(
        UUID userId,
        UUID channelId
) {}
