package com.sprint.mission.discodeit.web.dto;

import lombok.Getter;

import java.time.Instant;

@Getter
public class UserStatusUpdateDto {
    private Instant newAttendAt;
}
