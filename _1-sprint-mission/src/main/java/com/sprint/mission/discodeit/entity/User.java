package com.sprint.mission.discodeit.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.regex.Pattern;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseUpdatableEntity implements Serializable {

  private static final long serialVersionUID = 1L; //직렬화 버전
  //필드
  @Column(nullable = false, unique = true, length = 50)
  private String username; //유저이름

  @Column(nullable = false, unique = true, length = 100)
  private String email; //이메일(아이디)

  @Column(nullable = false, length = 60)
  private String password;//비밀번호

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "profile_id") // BinaryContent의 ID를 참조하는 컬럼
  private BinaryContent profile;

  @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private UserStatus status;

  //private transient String password;//비밀번호
  //프론트 조건에 맞춰야 하므로 주석처리
  //private String phoneNumber;//전화번호
  //private String address; //주소

  //사용자 검사 필드(클래스에서 변경되지 않는 공용 데이터)
  // 전화번호 010-0000-0000 만 허용
  //private static final Pattern PHONE_PATTERN = Pattern.compile("010-\\d{4}-\\d{4}");
  // 기본적인 이메일 형식
  private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
  //비밀번호 : 숫자 하나 이상, 알파벳 하나 이상, 특수문자 하나 이상, 8자 이상
  private static final Pattern PASSWORD_PATTERN = Pattern.compile(
      "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*]).{8,}$");

  //생성자
  public User(String username, String email, String password, BinaryContent profile) {
    super();
    //검증
    if (username == null || username.length() > 20) {
      throw new IllegalArgumentException("이름은 최대 20자까지 가능합니다.");
    }
    if (!EMAIL_PATTERN.matcher(email).matches()) {
      throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
    }
    if (!PASSWORD_PATTERN.matcher(password).matches()) {
      throw new IllegalArgumentException("비밀번호는 최소 8자이며 숫자, 문자, 특수문자를 포함해야 합니다.");
    }
    this.username = username;
    this.email = email;
    this.password = password;
    this.profile = profile;
    this.status = new UserStatus(this, Instant.now());
  }

  // update 메소드
  public void update(String newUsername, String newEmail, String newPassword,
      BinaryContent newProfile) {
    if (newUsername != null && !newUsername.trim().isEmpty() && !newUsername.equals(
        this.username)) {
      this.username = newUsername;
      updateTimestamp();
    }
    if (newEmail != null && !newEmail.trim().isEmpty() && !newEmail.equals(this.email)) {
      this.email = newEmail;
      updateTimestamp();
    }
    if (newPassword != null && !newPassword.trim().isEmpty() && !newPassword.equals(
        this.password)) {
      this.password = newPassword;
      updateTimestamp();
    }
    if (newProfile != null && !newProfile.equals(this.profile)) {
      this.profile = newProfile;
      updateTimestamp();
    }
  }

}
