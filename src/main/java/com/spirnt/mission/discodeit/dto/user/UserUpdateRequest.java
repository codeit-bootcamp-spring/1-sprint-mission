package com.spirnt.mission.discodeit.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "수정할 User 정보")
public class UserUpdateRequest {

  private String newUsername;
  private String newEmail;
  private String newPassword;

  public UserUpdateRequest(String newUsername, String newEmail,
      String newPassword) {
    this.newUsername = newUsername;
    this.newEmail = newEmail;
    this.newPassword = newPassword;
  }
}
