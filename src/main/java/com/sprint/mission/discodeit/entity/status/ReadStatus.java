package com.sprint.mission.discodeit.entity.status;

import lombok.Getter;

import java.time.Instant;

@Getter
public class ReadStatus {
    private String id;
    private Instant createdAt;
    private Instant updatedAt;
}
