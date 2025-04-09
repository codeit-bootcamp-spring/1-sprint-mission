package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseUpdatableEntity implements Serializable {

  private static final Long serialVersionUID = 1L;

  @Column(name = "username")
  private String username;

  @Column(name = "email")
  private String email;

  @Column(name = "password")
  private String password;

  @OneToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "profile_id")
  private BinaryContent profile;

  @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST, orphanRemoval = true)
  private UserStatus status;
  //update

  public void updateUser(String username, String email, String password,
      BinaryContent nullableProfile) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.profile = nullableProfile;

    super.update();
  }

  //새로운 이미지가 들어오면, 완전히 새로운 이미지 객체로 간주 ?
  private void updateBinaryContent(BinaryContent newBinaryContent) {
    this.profile = newBinaryContent;
  }


}
