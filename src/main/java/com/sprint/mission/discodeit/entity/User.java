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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
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


  protected User() {
    super();
  }

  public User(UserCreateDTO userCreateDTO) {
    super();

    this.username = userCreateDTO.name();
    this.password = userCreateDTO.password();
    this.email = userCreateDTO.email();
    updateBinaryContent(userCreateDTO.filePath());
  }

  //update

  public void updateUser(UserUpdateDTO userUpdateDTO) {
    updateUserName(userUpdateDTO.newName());
    updatePassword(userUpdateDTO.newPassword());
    updateEmail(userUpdateDTO.newEmail());
    updateBinaryContent(userUpdateDTO.newFilePAth());
    update();
  }

  private void updateUserName(String newName) {
    this.username = Objects.requireNonNullElse(newName, this.username);
  }

  private void updatePassword(String newPassword) {
    this.password = Objects.requireNonNullElse(newPassword, this.password);
  }

  private void updateEmail(String newEmail) {
    this.email = Objects.requireNonNullElse(newEmail, this.email);
  }

  //새로운 이미지가 들어오면, 완전히 새로운 이미지 객체로 간주 ?
  private void updateBinaryContent(String newFilePath) {
    if (newFilePath != null) {
      this.profile = new BinaryContent(newFilePath);
    }
  }

  //delete

  public void deleteBinaryContent() {
    this.profile = null;
  }


}
