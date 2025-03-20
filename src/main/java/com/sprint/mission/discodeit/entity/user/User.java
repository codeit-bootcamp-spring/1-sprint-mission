package com.sprint.mission.discodeit.entity.user;

import com.sprint.mission.discodeit.entity.base.baseUpdatableEntity;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.entity.status.user.UserStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Duration;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends baseUpdatableEntity {

  private String username;
  private String email;
  private String password;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "profile_id")
  private BinaryContent profile;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private UserStatus status;

  public void changeUserInfo(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }

  public void changeUserStatus(UserStatus userStatus) {
    this.status = userStatus;
    this.status.initUser(this);
  }

  public void changeProfile(BinaryContent profile) {
    this.profile = profile;
  }

  public boolean isThereHere() {
    Instant now = Instant.now();
    Duration between = Duration.between(status.getLastActiveAt(), now);

    return !(between.toMinutes() >= 5);
  }

}
