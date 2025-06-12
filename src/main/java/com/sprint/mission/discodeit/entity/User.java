package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseUpdatableEntity {

  @Enumerated(EnumType.STRING)
  private Role role;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @JoinColumn(name = "profile_id")
  private BinaryContent profile;

  @Builder
  public User(String username, String email, String password, BinaryContent profile, Role role) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.profile = profile;
    this.role = role;
  }

  public void updateProfile(BinaryContent profile) {
    this.profile = profile;
  }


  public void updateUser(String username, String email, String password, BinaryContent profile) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.profile = profile;
  }

  public void updateRole(Role role) {
    this.role = role;
  }


  //음.. 프록시를 위해서? 이게 맞나
  public static User withId(UUID id) {
    User user = new User();
    user.setId(id);
    return user;
  }

}

