package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record MessageCreateRequest(
        UUID channelId,
        UUID writerId,
        String content
) {}
