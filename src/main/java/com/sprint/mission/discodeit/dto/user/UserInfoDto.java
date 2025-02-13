package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value(staticConstructor = "of")
public class UserInfoDto {
    UUID id;
    Instant createdAt;
    Instant updatedAt;

    String name;
    String email;
    UserStatus status;
}
