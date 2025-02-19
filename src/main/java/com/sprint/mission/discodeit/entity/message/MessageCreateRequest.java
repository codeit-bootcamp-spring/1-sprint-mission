package com.sprint.mission.discodeit.entity.message;

import com.sprint.mission.discodeit.entity.user.User;

public record MessageCreateRequest(
        String title,
        String content,
        User sender,
        User receiver
) {
}
