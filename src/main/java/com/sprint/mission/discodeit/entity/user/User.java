package com.sprint.mission.discodeit.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sprint.mission.discodeit.entity.BaseObject;
import com.sprint.mission.discodeit.exception.user.IllegalUserException;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends BaseObject implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @JsonProperty("username")
  private String userName;

  @JsonProperty("email")
  private String userEmail;

  @JsonProperty("password")
  private transient String userPassword;

  @JsonProperty("userRole")
  private UserRole userRole;

  @Setter
  private String profileId;

  public static User createUser(UUID userId, UserCommonRequest user, UserRole userRole) {
    return new User(userId, user.userName(), user.userEmail(), user.userPassword(), userRole);
  }

  public void updateUserInfo(String username, String email, String password, String profileId) {
    this.userName = username;
    this.userEmail = email;
    this.userPassword = password;
    this.profileId = profileId;
  }

  @Builder
  public User(String userName, String userEmail, String userPassword, String profileId) {
    this.userName = userName;
    this.userEmail = userEmail;
    this.userPassword = userPassword;
    this.profileId = profileId;
  }

  public User() {
  }

  public User(UUID userid) {
    super(userid);
  }

  public User(UUID userId, String userName, String userEmail) {
    super(userId);
    setUserName(userName);
    setUserEmail(userEmail);
  }

  public User(String userName, String userEmail, String userPassword, UserRole userRole) {
    super();
    setUserName(userName);
    setUserEmail(userEmail);
    setUserRole(userRole);
    setUserPassword(userPassword);
  }

  public User(UUID userId, String userName, String userEmail, String userPassword) {
    super(userId);
    setUserName(userName);
    setUserEmail(userEmail);
    setUserPassword(userPassword);
  }

  public User(UUID userId, String userName, String userEmail, String userPassword,
      UserRole userRole) {
    super(userId);
    setUserName(userName);
    setUserEmail(userEmail);
    setUserRole(userRole);
    setUserPassword(userPassword);
  }


  private void setUserRole(UserRole userRole) {
    if (userRole == null) {
      throw new IllegalUserException("user Role을 지정해주세요.");
    }

    this.userRole = userRole;
  }

  public void setUserName(String userName) {
    checkUserName(userName);
    this.userName = userName;
    setUpdatedAt();
  }

  public void setUserEmail(String userEmail) {
    checkUserEmail(userEmail);
    this.userEmail = userEmail;
    setUpdatedAt();
  }

  public void setUserPassword(String userPassword) {
    checkUserPassword(userPassword);
    this.userPassword = userPassword;
    setUpdatedAt();
  }

  private boolean checkUserName(String userName) {
    if (userName == null || userName.isEmpty()) {
      throw new IllegalUserException("이름을 작성해주세요.");
    }
    return true;
  }

  private boolean checkUserEmail(String userEmail) {
    final String EMAIL_REGEX = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+$";

    if (userEmail == null || userEmail.isEmpty()) {
      throw new IllegalUserException("이메일을 적어주세요.");
    } else if (!userEmail.matches(EMAIL_REGEX)) {
      throw new IllegalUserException("이메일 형식이 잘못되었습니다.");
    }

    return true;
  }


  private boolean checkUserPassword(String userPassword) {
    if (userPassword == null || userPassword.length() < 6) {
      throw new IllegalUserException("비밀번호가 잘못되었습니다.");
    }
    return true;
  }

  public String updateName(String updateName) {
    setUserName(updateName);
    return this.userName;
  }

  public UserRole updateUserRole(UserRole userRole) {
    setUserRole(userRole);
    return userRole;
  }

  public String updateEmail(String updateEmail) {
    setUserEmail(updateEmail);
    return this.userEmail;
  }

  public String updatePassword(String updatePassword) {
    setUserPassword(updatePassword);
    return this.userPassword;
  }

  @Override
  public String toString() {
    return "User{" +
        "id=" + getId() +
        ", name='" + userName + '\'' +
        ", email='" + userEmail + '\'' +
        ", createdAt=" + getCreatedAt() +
        ", updatedAt=" + getUpdatedAt() +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(getId(), user.getId()) &&
        Objects.equals(userName, user.userName) &&
        Objects.equals(userEmail, user.userEmail);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), userName, userEmail);
  }
}
