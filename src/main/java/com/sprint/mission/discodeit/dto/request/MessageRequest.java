package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public record MessageRequest(
        User writer,
        String content,
        UUID channelId
) {}
