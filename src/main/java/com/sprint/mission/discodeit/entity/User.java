package com.sprint.mission.discodeit.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class User implements Serializable {

  private static final long serialVersionUID = 1L;
  private UUID id;
  private Instant createdAt;
  private Instant updatedAt;

  private String username;
  private String email;
  private String password;
  private UUID profileId;

  public User(String username, String email, String password, UUID profileId) {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
    //
    this.username = username;
    this.email = email;
    this.password = password;
    this.profileId = profileId;
  }


  public void update(String username, String email, String password, UUID newProfileId) {
    boolean flag = false;
    if (username != null && !username.equals(this.username)) {
      this.username = username;
      flag = true;
    }
    if (email != null && !email.equals(this.email)) {
      this.email = email;
      flag = true;
    }
    if (password != null && !password.equals(this.password)) {
      this.password = password;
      flag = true;
    }
    if (newProfileId != null && !newProfileId.equals(this.profileId)) {
      this.profileId = newProfileId;
      flag = true;
    }

    if (flag) {
      this.updatedAt = Instant.now();
    }
  }

  @Override
  public String toString() {
    return "User{" +
        "name='" + username + '\'' +
        '}';
  }
}
