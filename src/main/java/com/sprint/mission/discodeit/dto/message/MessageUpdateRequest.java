package com.sprint.mission.discodeit.dto.message;

import java.util.UUID;

public record MessageUpdateRequest(
    UUID writerId,
    String newContent
) {

}
