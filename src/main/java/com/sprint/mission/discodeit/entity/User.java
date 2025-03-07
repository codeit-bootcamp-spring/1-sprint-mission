package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;

import java.time.Instant;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseUpdatableEntity {

  @Column(length = 50, unique = true, nullable = false)
  private String username;

  @Column(length = 100, unique = true, nullable = false)
  private String email;

  @Column(length = 60, nullable = false)
  private transient String password;

  @OneToOne(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "profile_id")
  private BinaryContent profile;

  @OneToOne(mappedBy = "user")
  private UserStatus status;

  public static User createUser(String name, String email, String password) {
    return new User(name, email, password);
  }

  private User(String name, String email, String password) {
    this.username = name;
    this.email = email;
    this.password = password;
  }

  public void update(String newName, String newEmail, String newPassword) {
    boolean isChanged = false;
    if (!newName.equals(this.username)) {
      this.username = newName;
      isChanged = true;
    }
    if (!newEmail.equals(this.email)) {
      this.email = newEmail;
      isChanged = true;
    }
    if (!newPassword.equals(this.password)) {
      this.password = newPassword;
      isChanged = true;
    }

    if (isChanged) {
      this.updatedAt = Instant.now();
    }
  }

  @Override
  public String toString() {
    return "User{" +
        "username='" + username + '\'' +
        ", email='" + email + '\'' +
        ", password='" + password + '\'' +
        ", profile=" + profile +
        ", status=" + status +
        ", updatedAt=" + updatedAt +
        '}';
  }
}
