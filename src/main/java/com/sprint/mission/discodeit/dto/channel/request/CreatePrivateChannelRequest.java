package com.sprint.mission.discodeit.dto.channel.request;

import java.util.UUID;

public record CreatePrivateChannelRequest(
	UUID userId1,
	UUID userId2
) {
}
