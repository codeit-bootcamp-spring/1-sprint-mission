package com.sprint.mission.discodeit.dto.userStatusDto;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.UUID;

public class FindUserStatusResponseDto {

    UUID id;
    Instant lastAccessTime;

    public FindUserStatusResponseDto(UserStatus userStatus) {
        this.id = userStatus.getId();
        this.lastAccessTime = userStatus.getLastAccessTime();
    }
}
