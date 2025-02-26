package com.sprint.mission.discodeit.dto.request;

import java.util.UUID;

public record MessageRequest(
        UUID writer,
        String content,
        UUID channelId
) {}
