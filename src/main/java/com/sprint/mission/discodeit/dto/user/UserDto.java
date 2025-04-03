package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.BinaryContent;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {

  private final UUID id;
  private final String name;
  private final String email;
  private final BinaryContent profile;
  private final Boolean online;

  public UserDto(UUID id, String name, String email, BinaryContent profile, boolean online) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.profile = profile;
    this.online = online;
  }

  @Override
  public String toString() {
    return "User[Name: " + this.getName() +
        " Email: " + this.getEmail() +
        " Profile: " + this.getProfile() +
        " ID: " + this.id + "]";
  }


}
