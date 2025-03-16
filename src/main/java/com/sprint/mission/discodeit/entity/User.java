package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users") // 'user'는 SQL 예약어이므로 'users'로 테이블명 지정
@Getter
public class User extends BaseUpdatableEntity {

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  // User -> BinaryContent (User가 주인)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "profile_id", nullable = true,
      foreignKey = @ForeignKey(name = "fk_user_profile"))
  private BinaryContent profile;

  // User <-> UserStatus (양방향, UserStatus가 주인)
  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private UserStatus status;

  // 부모-자식 관계에서 User가 부모인 리스트들
  @OneToMany(mappedBy = "author", cascade = {CascadeType.PERSIST,
      CascadeType.MERGE}, orphanRemoval = true)
  private List<Message> messages = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<ReadStatus> readStatuses = new ArrayList<>();


  protected User() {
    super();
  }

  public User(String username, String email, String password, BinaryContent profile) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.profile = profile;
  }

  public void update(String newUsername, String newEmail, String newPassword,
      BinaryContent newProfile) {
    boolean anyValueUpdated = false;
    if (newUsername != null && !newUsername.equals(this.username)) {
      this.username = newUsername;
      anyValueUpdated = true;
    }
    if (newEmail != null && !newEmail.equals(this.email)) {
      this.email = newEmail;
      anyValueUpdated = true;
    }
    if (newPassword != null && !newPassword.equals(this.password)) {
      this.password = newPassword;
      anyValueUpdated = true;
    }
    if (newProfile != null && !newProfile.equals(this.profile)) {
      this.profile = newProfile;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      setUpdatedAt(Instant.now());
    }
  }

  // UserStatus와의 양방향 관계를 위한 헬퍼 메서드
  public void setStatus(UserStatus status) {
    this.status = status;
  }

  // 자식 컬렉션 관리를 위한 헬퍼 메서드
  public void addMessage(Message message) {
    if (!this.messages.contains(message)) {
      this.messages.add(message);
    }
  }

  public void addReadStatus(ReadStatus readStatus) {
    if (!this.readStatuses.contains(readStatus)) {
      this.readStatuses.add(readStatus);
    }
  }
}