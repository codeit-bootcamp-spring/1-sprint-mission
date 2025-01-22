package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.constant.UserConstant;
import com.sprint.mission.discodeit.exception.UserValidationException;
import com.sprint.mission.discodeit.util.UuidGenerator;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
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
  private String profilePictureURL;
  private String description;
  private Long createdAt;
  private Long updatedAt;

  private User(UserBuilder builder) {
    this.UUID = UuidGenerator.generateUUID();
    this.username = builder.username;
    this.password = builder.password;
    this.email = builder.email;
    this.nickname = builder.nickname;
    this.phoneNumber = builder.phoneNumber;
    this.profilePictureURL = builder.profilePictureURL != null ? builder.profilePictureURL : UserConstant.DEFAULT_PROFILE_PICTURE_URL;
    this.description = builder.description;
    this.createdAt = System.currentTimeMillis();
    this.updatedAt = System.currentTimeMillis();
  }

  public static class UserBuilder {
    private final String username;
    private final String password;
    private final String email;
    private String nickname;
    private String phoneNumber;
    private String profilePictureURL;
    private String description;

    public UserBuilder(String username, String password, String email) throws UserValidationException {
      this.username = username;
      this.password = password;
      this.email = email;
    }

    public UserBuilder nickname(String nickname) throws UserValidationException {

      this.nickname = nickname;
      return this;
    }

    public UserBuilder phoneNumber(String phoneNumber) {
      //TODO : 핸드폰 번호 검증 로직
      this.phoneNumber = phoneNumber;
      return this;
    }

    public UserBuilder profilePictureURL(String profilePictureURL) {
      this.profilePictureURL = profilePictureURL;
      return this;
    }

    public UserBuilder description(String description) {
      this.description = description;
      return this;
    }

    public User build() {
      return new User(this);
    }
  }

  @Override
  public String toString() {
    return
        ", username='" + username + '\'' +
        ", password='" + password + '\'' +
        ", email='" + email + '\'' +
        ", nickname='" + nickname + '\'' +
        ", phoneNumber='" + phoneNumber + '\'' +
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
