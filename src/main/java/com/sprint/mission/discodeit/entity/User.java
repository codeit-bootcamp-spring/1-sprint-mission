package com.sprint.mission.discodeit.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.security.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "users")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseUpdatableEntity {

  @Column(nullable = false, unique = true, length = 50)
  private String username;

  @Column(nullable = false, unique = true, length = 100)
  private String email;

  @Column(nullable = false)
  private String password;

  @OneToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.SET_NULL)
  @JoinColumn(name = "profile_id")
  private BinaryContent profile;

  @JsonManagedReference
  @OneToOne(mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
  private UserStatus status;

  @Enumerated(EnumType.STRING)
  private Role role;

  public User(String username, String email, String password, BinaryContent profile) {
    this.username = username;
    this.email = email;
    this.password = password;

    this.profile = profile;
    this.status = new UserStatus(this, Instant.now());
    this.role = Role.ROLE_USER;
  }

  public static User createAdmin(String username, String email, String password) {
    User user = new User(username, email, password, null);
    user.role = Role.ROLE_ADMIN;
    return user;
  }

  public void updateProfile(BinaryContent profile) {
    if (!this.profile.getId().equals(profile.getId())) {
      this.profile = profile;
    }
  }

  public void updateName(String username) {
    if (!this.username.equals(username)) {
      this.username = username;
    }
  }

  public void updateEmail(String email) {
    if (!this.email.equals(email)) {
      this.email = email;
    }
  }

  public void updatePassword(String newPassword, PasswordEncoder encoder) {
    if (!encoder.matches(newPassword, this.password)) {
      this.password = encoder.encode(password);
    }
  }

  public void updateRole(Role newRole) {
    if (!this.role.equals(newRole)) {
      this.role = newRole;
    }
  }

  public boolean isSamePassword(String password, PasswordEncoder encoder) {
    return encoder.matches(password, this.password);
  }

  public void validateDuplicateName(String name) {
    if (this.username.equals(name)) {
      throw new IllegalArgumentException("[ERROR] 이미 존재하는 이름입니다.");
    }
  }

  public void validateDuplicateEmail(String email) {
    if (this.email.equals(email)) {
      throw new IllegalArgumentException("[ERROR] 이미 존재하는 이메일입니다.");
    }
  }

  @Override
  public String toString() {
    return String.format(
        username + "님의 정보입니다." + System.lineSeparator()
            + "Name: " + username + System.lineSeparator()
            + "Email: " + email + System.lineSeparator()
    );
  }
}
