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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User extends BaseUpdatableEntity {

  //로그인 아이디
  @Column(length = 50, nullable = false, unique = true)
  private String username;
  //이메일 - 로그인용 계정 아이디
  @Column(length = 100, nullable = false, unique = true)
  private String email;
  //비밀번호
  @Column(length = 60, nullable = false)
  private String password;
  //사용자 프로필 사진
  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @JoinColumn(name = "profile_id", columnDefinition = "uuid")
  private BinaryContent profile;
  //유저 역할
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  public User(String username, String email, String password,
      BinaryContent profile) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.profile = profile;
    this.role = Role.USER;
  }
}
