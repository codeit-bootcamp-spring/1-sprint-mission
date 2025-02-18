package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

public record MessageDto(
        User writer,
        String content,
        Channel channel
) {}
