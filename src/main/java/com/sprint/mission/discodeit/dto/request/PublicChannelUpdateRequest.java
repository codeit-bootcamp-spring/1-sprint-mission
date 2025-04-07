package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PublicChannelUpdateRequest(
	@NotBlank
    String newName,
    String newDescription
) {

}
