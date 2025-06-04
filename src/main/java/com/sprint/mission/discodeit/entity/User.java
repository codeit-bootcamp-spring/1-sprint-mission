package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.common.Phone;
import com.sprint.mission.discodeit.security.role.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseUpdatableEntity {

  @Column(name = "username", length = 50, unique = true, nullable = false)
  private String username;

  @Column(name = "nickname", length = 50, nullable = false)
  private String nickname;

  @Column(name = "email", length = 100, unique = true, nullable = false)
  private String email;

  @Column(name = "password", length = 200, nullable = false)
  private String password;

  @Embedded
  private Phone phone;

  @Column(name = "role", length = 20, nullable = false)
  @Enumerated(EnumType.STRING)
  private Role role;

  @Column(name = "is_active", nullable = false)
  private boolean status = true;

  @Column(name = "introduce")
  private String introduce;

  @Column(name = "is_account_non_locked")
  private boolean accountNonLocked = true; // 계정 잠김 여부

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @JoinColumn(name = "profile_id", foreignKey = @ForeignKey(name = "fk_profile"), nullable = true)
  @OnDelete(action = OnDeleteAction.SET_NULL)
  private BinaryContent profile;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ChannelMember> joinedChannels = new ArrayList<>();

  // 생성자
  public User(
      String username, String nickname, String email, String password,
      Phone phone, Role role, String introduce, BinaryContent profile
  ) {
    this.username = username;
    this.nickname = nickname;
    this.email = email;
    this.password = password;
    this.phone = phone;
    this.role = role;
    this.introduce = introduce;
    this.profile = profile;
  }

  // Update
  public void updateUsername(String username) {
    this.username = username;
  }

  public void updateNickname(String nickname) {
    this.nickname = nickname;
  }

  public void updateEmail(String email) {
    this.email = email;
  }

  public void updatePassword(String password) {
    this.password = password;
  }

  public void updatePhone(Phone phone) {
    this.phone = phone;
  }

  public void updateRole(Role role) {
    this.role = role;
  }

  public void updateStatus(boolean status) {
    this.status = status;
  }

  public void updateIntroduce(String introduce) {
    this.introduce = introduce;
  }

  public void updateProfile(BinaryContent profile) {
    this.profile = profile;
  }

//  public void updateUserStatus(UserStatus userStatus) {
//    this.userStatus = userStatus;
//  }

  @Override
  public String toString() {
    return "User{" +
        "username='" + username + '\'' +
        ", nickname='" + nickname + '\'' +
        ", email=" + email +
        ", phone=" + phone +
        ", userType=" + role +
        ", status=" + status +
        ", introduce='" + introduce + '\'' +
        ", createdAt='" + getCreatedAt() + '\'' +
        '}';
  }
}
