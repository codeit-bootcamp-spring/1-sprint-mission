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

import java.io.Serializable;
import java.time.Instant;
import java.util.regex.Pattern;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseUpdatableEntity {

  //필드
  @Column(length = 50, nullable = false, unique = true)
  private String username; //유저이름

  @Column(length = 100, nullable = false, unique = true)
  private String email; //이메일

  @Column(length = 60, nullable = false)
  private String password; //비밀번호

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @JoinColumn(name = "profile_id", columnDefinition = "uuid")
  private BinaryContent profile;

  @JsonManagedReference //순환 참조 문제를 해결
  @Setter(AccessLevel.PROTECTED)
  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private UserStatus status;

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
