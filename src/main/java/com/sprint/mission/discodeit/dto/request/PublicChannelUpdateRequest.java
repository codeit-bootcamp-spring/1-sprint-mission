package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Size;

public record PublicChannelUpdateRequest(
    @Size(min = 1, max = 50)
    String newName,
    @Size(max = 500)
    String newDescription
) {

}
