package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.Getter;

import java.util.UUID;

@Getter
public class User extends BaseUpdatableEntity {

  private String username;
  private String email;
  private String password;
  private UUID binaryContentId;

  public User(String username, String email, String password) {

    this.username = username;
    this.email = email;
    this.password = password;
  }

  public void updateBinaryContentId(UUID binaryContentId) {
    this.binaryContentId = binaryContentId;
  }


  public void setUser(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }

}

