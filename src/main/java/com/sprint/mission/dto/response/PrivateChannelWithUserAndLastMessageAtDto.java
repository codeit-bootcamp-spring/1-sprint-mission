package com.sprint.mission.dto.response;

import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.User;

import java.time.Instant;
import java.util.List;

public record PrivateChannelWithUserAndLastMessageAtDto(
        Channel channel,
        List<User> users,
        Instant lastMessageAt) {
}
