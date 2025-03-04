package com.spirnt.mission.discodeit.enity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;

@Getter
public class User extends Common implements Serializable {

  private static final long serialVersionUID = 1L;
  private String username;
  private String email;
  @JsonIgnore // 비밀번호 노출 방지
  private String password;
  private UUID profileId;


  public User(String username, String email, String password, UUID profileId) {
    super();
    this.username = username;
    this.email = email;
    this.password = password;
    this.profileId = profileId;
  }


  public void update(String name, String email, String password, UUID profileImageId) {
    boolean anyValueUpdated = false;
    if (name != null && !name.equals(this.username)) {
      this.username = name;
      anyValueUpdated = true;
    }
    if (email != null && !email.equals(this.email)) {
      this.email = email;
      anyValueUpdated = true;
    }
    if (password != null && !password.equals(this.password)) {
      this.password = password;
      anyValueUpdated = true;
    }
    if (profileImageId != null && !profileImageId.equals(this.profileId)) {
      this.profileId = profileImageId;
      anyValueUpdated = true;
    }
    if (anyValueUpdated) {
      this.updateClass(Instant.now());
    }
  }

  @Override
  public String toString() {
    return "User{" +
        "id='" + this.getId() + '\'' +
        ", username='" + this.username + '\'' +
        ", email='" + this.email + '\'' +
        '}';
  }

  // UUID만으로 객체를 비교하기 위해 추가
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    User user = (User) obj;
    return Objects.equals(this.getId(), user.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getId());
  }

}
