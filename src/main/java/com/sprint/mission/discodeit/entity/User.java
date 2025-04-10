package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.entity.status.AccountStatus;
import com.sprint.mission.discodeit.entity.status.UserStatus;
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
  //닉네임
  private String nickname;
  //이메일 - 로그인용 계정 아이디
  private String email;
  //비밀번호
  private String password;
  //사용자 설정 상태 메세지
  private String statusMessage;
  //계정 상태 - 인증완료, 미인증, 정지, 휴면 등
  private AccountStatus accountStatus;
  //사용자 프로필 사진
  @ManyToOne
  @JoinColumn(name = "profile_id")
  private BinaryContent profile;
  //유저 상태
  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private UserStatus status;

  public User(String username, String nickname, String email, String password, String statusMessage,
      AccountStatus accountStatus, BinaryContent profile) {
    this.username = username;
    this.nickname = nickname;
    this.email = email;
    this.password = password;
    this.statusMessage = statusMessage;
    this.accountStatus = accountStatus;
    this.profile = profile;
  }
}
