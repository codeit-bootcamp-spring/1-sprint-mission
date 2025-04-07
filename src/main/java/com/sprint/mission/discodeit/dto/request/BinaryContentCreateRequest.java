package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;

public record BinaryContentCreateRequest(
	@NotBlank
    String fileName,
	@NotBlank
    String contentType,
    byte[] bytes
) {

}
