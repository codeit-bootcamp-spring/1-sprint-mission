package com.sprint.mission.discodeit.dto.data;

import jakarta.persistence.criteria.CriteriaBuilder.In;
import java.time.Instant;
import java.util.UUID;

public record UserStatusDto(UUID id,Instant createAt,Instant updatedAt,UUID userId, Instant lastActiveAt) {

}
