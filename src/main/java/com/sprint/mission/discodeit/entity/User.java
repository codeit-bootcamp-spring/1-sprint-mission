package com.sprint.mission.discodeit.entity;

// User 파일이 Gender 파일과 같은 패키지 안에 있으므로 따로 임포트하지 않아도 됨

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User extends BaseUpdatableEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(nullable = false, unique = true, length = 50)
  private String username; // TODO : int같은 기본 타입은 null이 될 수가 없더라도 @NonNull로 명시를 해줘야 @RequiredArgsConstructor가 인식을 하는거 아닌가..?
  @Column(nullable = false, unique = true, length = 100)
  private String email;
  @Column(nullable = false, length = 60)
  private String password;
  @OneToOne(orphanRemoval = true, fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.SET_NULL)
  private BinaryContent profile;
  @OneToOne(mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
  @JoinColumn(name = "userStatusId")
  // 양방향은 mapped by로 주인관계 표현(비주인을 mapped by에 지정). 주인=FK 갖고 있는 엔티티. FK는 userStatus 엔티티가 가짐
  private UserStatus userStatus;


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
    if (newProfile != null && !newProfile.getId().equals(this.profile.getId())) {
      this.profile = newProfile;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      this.updatedAt = Instant.now();
    }
  }
}


