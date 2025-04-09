package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Size;

public record PublicChannelUpdateRequest(
	@Size(max = 100, message = "새 채널 이름은 최대 100자까지 가능합니다.")
	String newName,

	@Size(max = 500, message = "새 설명은 최대 500자까지 가능합니다.")
	String newDescription
) {
}