package com.sprint.mission.discodeit.dto.readStatus.request;

import java.time.Instant;

public record UpdateReadStatusRequest(
	Instant lastReadAt
) {
}
