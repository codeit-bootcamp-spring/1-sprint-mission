package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
  private String username;
  //이메일 - 로그인용 계정 아이디
  private String email;
  //비밀번호
  private String password;
  //사용자 프로필 사진
  @ManyToOne
  @JoinColumn(name = "profile_id")
  private BinaryContent profile;
  //유저 역할
  private String role;

  public User(String username, String email, String password,
      BinaryContent profile) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.profile = profile;
    this.role = "ROLE_USER";
  }
}
