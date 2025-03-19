package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UserRequestDTO {

  private final UUID id;
  private final String name;
  private final String email;
  private final BinaryContent filePath;
  private final Boolean isOnline;

  public UserRequestDTO(User user, boolean isOnline) {
    this.id = user.getId();
    this.name = user.getUsername();
    this.email = user.getEmail();
    this.filePath = user.getProfile();
    this.isOnline = isOnline;
  }

  @Override
  public String toString() {
    return "User[Name: " + this.getName() +
        " Email: " + this.getEmail() +
        " FilePath: " + this.getFilePath() +
        " ID: " + this.id + "]";
  }


}
