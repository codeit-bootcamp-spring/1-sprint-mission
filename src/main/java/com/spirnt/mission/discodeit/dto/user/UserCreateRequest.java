package com.spirnt.mission.discodeit.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "User 생성 정보")
public class UserCreateRequest {

  private String username;
  private String email;
  private String password;

  public UserCreateRequest(String username, String email,
      String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }
}
