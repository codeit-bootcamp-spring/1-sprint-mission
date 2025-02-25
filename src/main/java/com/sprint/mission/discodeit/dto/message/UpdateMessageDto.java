package com.sprint.mission.discodeit.dto.message;

import java.time.Instant;
import java.util.List;

public record UpdateMessageDto(
        String newContent,
        Instant updatedAt,
        List<String> binaryContentIds
){
}
