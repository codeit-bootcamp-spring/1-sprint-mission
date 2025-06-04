package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "users")
@Getter
@Builder
@AllArgsConstructor
public class User extends BaseUpdatableEntity {

  @Column(nullable = false, length = 50)
  private String username;

  @Column(nullable = false, length = 100)
  private String email;

  @Column(nullable = false, length = 60)
  private String password;

  @OneToOne
  @JoinColumn(name = "profile_id")
  private BinaryContent profile;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  public User(String username, String email, String password, BinaryContent profile) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.profile = profile;
    this.role = Role.USER;
  }

  // JPA용 기본 생성자, JPA만 접근할 수 있도록 protected 접근자 설정
  protected User() {
  }

  public void updateUsername(String newUsername) {
    this.username = newUsername;
  }

  public void updateEmail(String newEmail) {
    this.email = newEmail;
  }

  public void updatePassword(String newPassword) {
    this.password = newPassword;
  }

  public void updateProfile(BinaryContent newProfile) {
    this.profile = newProfile;
  }

  public void updateRole(Role newRole) {
    if (this.role != newRole) {
      this.role = newRole;
    }
  }
}
