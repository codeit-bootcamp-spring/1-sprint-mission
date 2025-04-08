package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Size;

public record PublicChannelCreateRequest(
    @Size(min = 1, max = 100)
    String name,
    @Size(max = 500)
    String description
) {

}
