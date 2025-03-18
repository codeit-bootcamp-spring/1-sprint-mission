package com.sprint.mission.discodeit.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class UserUpdateRequest {

  private String newUsername;
  private String newEmail;
  private String newPassword;
}
