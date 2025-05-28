package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseUpdatableEntity {

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  @ColumnDefault("ROLE_USER")
  @Enumerated(EnumType.STRING)
  private Role role;

  public enum Role implements GrantedAuthority {
    ROLE_ADMIN, ROLE_CHANNEL_MANAGER, ROLE_USER;

    @Override
    public String getAuthority() {
      return this.name();
    }
  }

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "profile_id")
  private BinaryContent profile;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private UserStatus status;

  @Builder(access = AccessLevel.PRIVATE)
  private User(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.role = Role.ROLE_USER;
  }

  public static User create(String username, String email, String password) {
    return User.builder()
        .username(username)
        .email(email)
        .password(password)
        .build();
  }

  protected void setUserStatus(UserStatus status) {
    this.status = status;
  }

  public void updateEmail(String email) {
    this.email = email;
  }

  public void updatePassword(String password) {
    this.password = password;
  }

  public void updateName(String username) {
    this.username = username;
  }

  public void updateRole(Role role) {
    this.role = role;
  }

  public void updateProfile(BinaryContent profile) {
    this.profile = profile;
  }

  public Optional<BinaryContent> getProfile() {
    return Optional.ofNullable(profile);
  }
}
