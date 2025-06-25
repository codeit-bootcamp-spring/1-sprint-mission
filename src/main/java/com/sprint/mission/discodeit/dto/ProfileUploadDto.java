package com.sprint.mission.discodeit.dto;

import java.util.UUID;
import lombok.Builder;

@Builder
public record ProfileUploadDto(
    UUID id,
    byte[] bytes,
    UUID userId
) {

}
