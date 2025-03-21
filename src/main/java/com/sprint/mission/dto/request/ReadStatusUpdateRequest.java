package com.sprint.mission.dto.request;

import java.time.Instant;

public record ReadStatusUpdateRequest(
    Instant newLastReadAt) {

}
