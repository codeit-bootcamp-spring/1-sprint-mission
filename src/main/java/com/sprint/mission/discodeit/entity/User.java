package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.constant.UserConstant;
import com.sprint.mission.discodeit.exception.UserValidationException;
import com.sprint.mission.discodeit.util.UuidGenerator;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
public class User implements Serializable {
  private static final long serialVersionUID = 1L;

  private String UUID;
  private String username;
  private String password;
  private String email;
  private String nickname;
  private String phoneNumber;
  private String binaryContentId;
  private String description;
  private String userStatusId;
  private Instant createdAt;
  private Instant updatedAt;

  private User(UserBuilder builder) {
    this.UUID = UuidGenerator.generateUUID();
    this.username = builder.username;
    this.password = builder.password;
    this.email = builder.email;
    this.nickname = builder.nickname;
    this.phoneNumber = builder.phoneNumber;
    this.binaryContentId = builder.binaryContentId;
    this.description = builder.description;
    this.userStatusId = builder.userStatusId;
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
  }

  public static class UserBuilder {
    private final String username;
    private final String password;
    private final String email;
    private String userStatusId;
    private String nickname;
    private String phoneNumber;
    private String binaryContentId;
    private String description;

    public UserBuilder(String username, String password, String email, String phoneNumber) throws UserValidationException {
      this.username = username;
      this.password = password;
      this.email = email;
      this.phoneNumber = phoneNumber;
    }

    public UserBuilder nickname(String nickname) throws UserValidationException {
      this.nickname = nickname;
      return this;
    }

    public UserBuilder binaryContentId(String binaryContentId ) {
      this.binaryContentId = binaryContentId;
      return this;
    }

    public UserBuilder description(String description) {
      this.description = description;
      return this;
    }

    public UserBuilder userStatusId(String id){
      this.userStatusId = id;
      return this;
    }

    public User build() {
      return new User(this);
    }
  }

  @Override
  public String toString() {
    return
        "USER: username='" + username + '\'' +
        ", email='" + email + '\'' +
        ", nickname='" + nickname + '\'' +
        ", phoneNumber='" + phoneNumber + '\'' + ", binaryID: " + binaryContentId +
        '}';
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
