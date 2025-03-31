package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.PastOrPresent;
import java.time.Instant;

public record ReadStatusUpdateRequest(
    @PastOrPresent(message = "The new last read time must be in the past or the present")
    Instant newLastReadAt
) {

}
