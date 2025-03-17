package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdateEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="users")
public class User extends BaseUpdateEntity {
  //
  private String username;
  private String email;
  private String password;

  @JoinColumn(name = "binary_content")
  private UUID profileImage;// BinaryContent

  public User(String username, String email, String password, UUID profileImage) {

    this.username = username;
    this.email = email;
    this.password = password;
    this.profileImage = profileImage;
  }
  protected User() {

  }

  public void update(String newUsername, String newEmail, String newPassword, UUID newProfileId) {
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
    if (newProfileId != null && !newProfileId.equals(this.profileImage)) {
      this.profileImage = newProfileId;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      setUpdatedAt( Instant.now());
    }

  }

}
