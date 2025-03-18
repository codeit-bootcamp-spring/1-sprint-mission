package com.sprint.mission.discodeit.dto.user.userStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class UserStatusUpdateRequest {

  private Instant newLastActiveAt;
}
