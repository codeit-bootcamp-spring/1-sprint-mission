package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseUpdatableEntity {

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @JoinColumn(name = "profile_id", columnDefinition = "uuid")
  private BinaryContent profile;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private UserStatus userStatus;

  public User(String username, String email, String password, BinaryContent profile) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.profile = profile;
  }

  public void update(String newUsername, String newEmail) {
    if (!newUsername.equals(this.username)) {
      this.username = newUsername;
    }
    if (!newEmail.equals(this.email)) {
      this.email = newEmail;
    }
  }

  public void updateProfile(BinaryContent newProfile) {
    this.profile = newProfile;
  }

}
