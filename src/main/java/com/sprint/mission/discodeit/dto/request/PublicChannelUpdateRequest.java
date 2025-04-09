package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Size;

public record PublicChannelUpdateRequest(
    @Size(min = 1) String newName,
    String newDescription
) {

}
