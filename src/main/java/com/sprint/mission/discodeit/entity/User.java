package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "users")
@Getter
@AllArgsConstructor // @Builder가 모든 필드를 받는 생성자를 필요로 한다
@Builder
public class User extends BaseUpdatableEntity {

  @Column(nullable = false, length = 50)
  private String username;

  @Column(nullable = false, length = 100)
  private String email;

  @Column(nullable = false, length = 60)
  private String password;

  @OneToOne
  @JoinColumn(name = "profile_id")
  private BinaryContent profile;

  // mappedBy : 비주인 객체, 컬럼 만들지 마. 읽기 전용
  @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
  // 읽기 전용으로 만들고,
  // users 테이블에는 컬럼을 만들지 않는다.
  // mappedBy ->> userStatus의 user 필드에 본 객체가 참조되도록 만든다
  private UserStatus userStatus;

  // JPA용 기본 생성자, JPA만 접근할 수 있도록 protected 접근자 설정
  protected User() {
  }

  public void updateUsername(String newUsername) {
    this.username = newUsername;
  }

  public void updateEmail(String newEmail) {
    this.email = newEmail;
  }

  public void updatePassword(String newPassword) {
    this.password = newPassword;
  }

  public void updateProfile(BinaryContent newProfile) {
    this.profile = newProfile;
  }
}
