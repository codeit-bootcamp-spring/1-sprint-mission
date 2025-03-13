package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.User;

public record CreatePublicChannelRequestDto(User user, String name, String explanation) {
}
