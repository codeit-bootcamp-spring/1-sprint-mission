package com.sprint.mission.discodeit.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record MessageCreateRequest(
	@NotBlank
    String content,
	@NotBlank
    UUID channelId,
	@NotBlank
    UUID authorId
) {

}
