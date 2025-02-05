package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.security.Encryptor;
import com.sprint.mission.discodeit.util.Identifiable;
import java.io.Serializable;

public class User extends BaseEntity implements Serializable, Identifiable {
  private static final long serialVersionUID = 1L;
  private final Encryptor encryptor;
  private transient String password;
  private String name;
  
  public User(String password, String name, Encryptor encryptor) {
    super();
    this.password = encryptor.getEncryptedPassword(password);
    this.name = name;
    this.encryptor = encryptor;
  }
  
  public String getPassword() {
    return password;
  }
  
  public String getName() {
    return name;
  }
  
  public void updatePassword(String password) {
    this.password = encryptor.getEncryptedPassword(password);
    super.update();
  }
  
  public void updateName(String name) {
    this.name = name;
    super.update();
  }
}

