package com.sprint.mission.discodeit.dto.user;


import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class UserDto {

  private UUID id;
  private String username;
  private String email;
  private Instant createdAt;
  private Instant updatedAt;
  ;
  private UUID profileId;
  private boolean Online;

  public UserDto(UUID id, String username, String email, Instant createdAt, Instant updatedAt,
      UUID profileId, boolean Online) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.profileId = profileId;
    this.Online = Online;
  }
}
