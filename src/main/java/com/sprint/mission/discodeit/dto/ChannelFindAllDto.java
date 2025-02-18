package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Channel;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ChannelFindAllDto(List<Channel> channels, Map<UUID, Instant> latestMessagesInstant, List<List<UUID>> userList) {

}
