package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity implements Serializable {

  private String name;
  private String nickname;
  private String email;
  private String password;
  private BinaryContent profileImage = BinaryContent.EMPTY;

  private User(String name, String nickname, String email, String password) {
    super();
    this.name = name;
    this.nickname = nickname;
    this.email = email;
    this.password = password;
  }

  public static User of(String name, String nickname, String email, String password) {
    return new User(name, nickname, email, password);
  }

  public void updateName(String name) {
    this.name = name;
    this.updateUpdatedAt();
  }

  public void updateNickname(String nickname) {
    this.nickname = nickname;
    this.updateUpdatedAt();
  }

  public void updateEmail(String email) {
    this.email = email;
    this.updateUpdatedAt();
  }

  public void updatePassword(String password) {
    this.password = password;
    this.updateUpdatedAt();
  }

  public void updateProfileImage(BinaryContent profileImage) {
    this.profileImage = profileImage;
    this.updateUpdatedAt();
  }

  public void updateUserInfo(String name, String email, String password) {
    this.updateName(name);
    this.updateEmail(email);
    this.updatePassword(password);
  }

  @Override
  public String toString() {
    return "User{" +
        "name='" + name + '\'' +
        ", nickname='" + nickname + '\'' +
        ", email='" + email + '\'' +
        ", password='" + password + '\'' +
        ", profileImage=" + profileImage +
        '}';
  }
}
