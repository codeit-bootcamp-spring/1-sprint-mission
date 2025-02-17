package com.sprint.mission.discodeit.channel.dto.request.channel;

import java.util.List;
import java.util.UUID;

public record UpdateChannelRequest(
	String name,
	String description,
	List<UUID> participantIds
) {
}
