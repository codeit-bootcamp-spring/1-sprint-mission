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

  @OneToOne(orphanRemoval = true) // 참조 제거 시 제거?
  @JoinColumn(name = "profile_id")
  private BinaryContent profile;

  @OneToOne(mappedBy = "user")
  private UserStatus status;

  public static User createUser(String name, String email, String password, BinaryContent profile) {
    return new User(name, email, password, profile);
  }

  private User(String name, String email, String password, BinaryContent profile) {
    this.username = name;
    this.email = email;
    this.password = password;
    this.profile = profile;
  }

  public void updateName(String username) {
    this.username = username;
  }

  public void updateEmail(String email) {
    this.email = email;
  }

  public void updatePassword(String password) {
    this.password = password;
  }

  public void updateProfile(BinaryContent profile) {
    this.profile = profile;
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
