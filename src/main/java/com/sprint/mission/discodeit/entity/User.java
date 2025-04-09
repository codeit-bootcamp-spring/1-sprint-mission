package com.sprint.mission.discodeit.entity;


import static com.sprint.mission.discodeit.constant.UserConstant.EMAIL_REGEX;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseUpdatableEntity {

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @JoinColumn(name = "profile_id")
  private BinaryContent profile;

  @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.LAZY)
  // nullable 고려

  private UserStatus status;


  public void updateFields(
      String username,
      String email,
      String password
  ) {
    if (username != null) {
      this.username = username;
    }
    if (email != null && email.matches(EMAIL_REGEX)) {
      this.email = email;
    }
    if (password != null) {
      this.password = password;
    }

  }

  public void updateStatus(UserStatus status) {
    this.status = status;
  }

  public void updateProfileImage(BinaryContent profile) {
    this.profile = profile;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;

    return Objects.equals(getId(), user.getId());

  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}
