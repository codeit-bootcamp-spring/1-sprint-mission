package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.exception.UserValidationException;
import com.sprint.mission.discodeit.util.UuidGenerator;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
public class User implements Serializable {
  private static final long serialVersionUID = 1L;

  private final String UUID;
  private String username;
  private String password;
  private String email;
  private String nickname;
  private String phoneNumber;
  private String description;

  private Instant createdAt;
  private Instant updatedAt;

  private BinaryContent profileImage;
  private UserStatus status;

  public User(String username, String password, String email, String nickname, String phoneNumber, String description) {
    this.UUID = UuidGenerator.generateUUID();
    this.username = username;
    this.password = password;
    this.email = email;
    this.nickname = nickname;
    this.phoneNumber = phoneNumber;
    this.description = description;
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
    this.profileImage = null;
    this.status = null;
  }
  @Override
  public String toString() {
    return
        "USER: username='" + username + '\''
            + ", email='" + email + '\''
            + ", nickname='" + nickname + '\''
            + ", phoneNumber='" + phoneNumber + '\''
            + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(UUID, user.UUID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(UUID);
  }
}
