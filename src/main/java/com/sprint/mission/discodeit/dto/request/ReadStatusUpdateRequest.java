package com.sprint.mission.discodeit.dto.request;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;

public record ReadStatusUpdateRequest(
	@NotBlank
    Instant newLastReadAt
) {

}
