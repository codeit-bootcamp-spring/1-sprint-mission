package com.sprint.mission.discodeit.dto.channel;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class FindPublicChannelResponse {
    String name;
    String topic;
    Instant recentMessageTime;
}
