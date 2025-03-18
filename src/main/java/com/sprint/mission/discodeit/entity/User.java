package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Table(name = "users")
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;

  @Column(nullable = false, length = 50)
  private String username;

  @Column(nullable = false, length = 100)
  private String email;

  @Column(nullable = false, length = 60)
  private String password;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "profile_id")
  private BinaryContent profile;

  public User(String username, String email, String password, BinaryContent profile) {
    this.createdAt = Instant.now();
    this.username = username;
    this.email = email;
    this.password = password;
    this.profile = profile;
  }

  public void update(String newUsername, String newEmail, String newPassword, BinaryContent newProfile) {
    if (newUsername != null && !newUsername.equals(this.username)) {
      this.username = newUsername;
      this.updatedAt = Instant.now();
    }
    if (newEmail != null && !newEmail.equals(this.email)) {
      this.email = newEmail;
      this.updatedAt = Instant.now();
    }
    if (newPassword != null && !newPassword.equals(this.password)) {
      this.password = newPassword;
      this.updatedAt = Instant.now();
    }
    if (newProfile != null && !newProfile.equals(this.profile)) {
      this.profile = newProfile;
      this.updatedAt = Instant.now();
    }
  }
}
