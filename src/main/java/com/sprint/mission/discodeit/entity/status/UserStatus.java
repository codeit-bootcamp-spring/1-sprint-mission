package com.sprint.mission.discodeit.entity.status;

import com.sprint.mission.discodeit.dto.userStatus.UpdateUserStatusDto;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable {
    //UserStatus의 id는 User의 식별자와 같습니다.
    private String id;
    private Instant createdAt;
    private Instant updatedAt;
    private static final int USER_ACTIVE_TIMEOUT_SECONDS = 5 * 60;

    public UserStatus(String userId) {
        id= userId;
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    public boolean isActive() {
        return Instant.now().minusSeconds(USER_ACTIVE_TIMEOUT_SECONDS).isBefore(updatedAt);
    }

    public boolean isUpdated(UpdateUserStatusDto updateUserStatusDto) {
        return updateUserStatusDto.updateAt() != updatedAt;
    }
}