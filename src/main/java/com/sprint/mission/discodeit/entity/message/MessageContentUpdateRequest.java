package com.sprint.mission.discodeit.entity.message;

import jakarta.validation.constraints.NotBlank;

public record MessageContentUpdateRequest(

    @NotBlank
    String newContent
) {

}
