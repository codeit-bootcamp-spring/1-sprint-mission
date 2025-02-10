package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.security.Encryptor;
import lombok.Getter;
import java.io.Serializable;
import java.util.Optional;

@Getter
public class User extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  private final Encryptor encryptor;
  private transient String password;
  private String name;
  private String email;
  private Optional<BinaryContent> profileImage;
  
  public User(String password, String name, String email, Encryptor encryptor,
              Optional<BinaryContent> profileImage) {
    super();
    this.password = encryptor.getEncryptedPassword(password);
    this.name = name;
    this.email = email;
    this.encryptor = encryptor;
    this.profileImage = profileImage;
  }
  
  public void updatePassword(String password) {
    this.password = encryptor.getEncryptedPassword(password);
    super.update();
  }
  
  public void updateName(String name) {
    this.name = name;
    super.update();
  }
  
  public void updateProfileImage(Optional<BinaryContent> newImage) {
    this.profileImage = newImage;
  }
}

