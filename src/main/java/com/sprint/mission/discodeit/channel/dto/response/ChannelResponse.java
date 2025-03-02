package com.sprint.mission.discodeit.channel.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.channel.entity.ChannelType;

public record ChannelResponse(UUID id,
							  ChannelType type,
							  String name,
							  String description,
							  List<UUID> participantIds,
							  Instant lastMessageAt) {
}
