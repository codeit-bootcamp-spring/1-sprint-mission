package com.sprint.mission.discodeit.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseUpdatableEntity {             // 유저 정보

  @Column(name = "username", length = 50, nullable = false, unique = true)
  private String username;    // 아이디

  @Column(name = "email", length = 100, nullable = false, unique = true)
  private String email;   // 이메일

  @Column(name = "password", length = 60, nullable = false)
  private String password;    // 비밀번호

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @JoinColumn(name = "profile_id", columnDefinition = "uuid")
  private BinaryContent profile;    // 프로필 사진

  @JsonManagedReference   // 순환 참조 문제 해결 - 부모
  @Setter(AccessLevel.PROTECTED)  // status 값을 같은 패키지나 하위 클래스에서는 수정 가능하나, 외부 클래스에서는 변경 불가
  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private UserStatus status;    // 유저 접속 상태

  // 생성자
  public User(String username, String email, String password, BinaryContent profile) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.profile = profile;
  }

  // 유저 수정
  public void update(String newUsername, String newEmail, String newPassword,
      BinaryContent newProfile) {

    if (newUsername != null && !newUsername.equals(this.username)) {
      this.username = newUsername;
    }
    if (newEmail != null && !newEmail.equals(this.email)) {
      this.email = newEmail;
    }
    if (newPassword != null && !newPassword.equals(this.password)) {
      this.password = newPassword;
    }
    if (newProfile != null) {
      this.profile = newProfile;
    }
  }
}