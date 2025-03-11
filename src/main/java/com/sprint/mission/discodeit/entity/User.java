package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.user.UserCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserUpdateDTO;
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
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

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

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "profile_id")
  private BinaryContent profile;

  //update

  public void updateUser(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
    update();
  }

  //새로운 이미지가 들어오면, 완전히 새로운 이미지 객체로 간주 ?
  private void updateBinaryContent(BinaryContent newBinaryContent) {
    this.profile = newBinaryContent;
  }

  //delete

  public void deleteBinaryContent() {
    this.profile = null;
  }


}
