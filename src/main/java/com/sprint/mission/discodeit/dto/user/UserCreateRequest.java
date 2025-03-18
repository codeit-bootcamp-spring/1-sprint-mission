package com.sprint.mission.discodeit.dto.user;

import lombok.Getter;

@Getter
public class UserCreateRequest {

  private String username;
  private String email;
  private String password;

  public UserCreateRequest(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }
}
