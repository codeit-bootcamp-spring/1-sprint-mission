package com.sprint.mission.discodeit.readStatus.dto;

import java.time.Instant;

public record ReadStatusUpdateRequest(
	Instant newLastReadAt
) {

}

