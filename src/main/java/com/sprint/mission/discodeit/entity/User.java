package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.security.Encryptor;
import lombok.Getter;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

@Getter
public class User extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  private transient String enctyptedPassword;
  private String salt;
  private String name;
  private String email;
  private Optional<BinaryContent> profileImage;
  
  public User(String encryptedPassword, String salt, String name, String email,
              Optional<BinaryContent> profileImage) {
    super();
    this.enctyptedPassword = Objects.requireNonNull(encryptedPassword, "password cannot be null");
    this.salt = Objects.requireNonNull(salt, "salt cannot be null");;
    this.name = name;
    this.email = email;
    this.profileImage = profileImage;
  }
  
  public void updatePassword(String enctyptedPassword, String salt) {
    this.enctyptedPassword = Objects.requireNonNull(enctyptedPassword, "password cannot be null");
    this.salt = Objects.requireNonNull(salt, "salt cannot be null");;
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

