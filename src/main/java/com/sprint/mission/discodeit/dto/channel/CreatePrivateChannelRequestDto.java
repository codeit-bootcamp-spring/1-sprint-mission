package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public record CreatePrivateChannelRequestDto(UUID ownerId) {
}
