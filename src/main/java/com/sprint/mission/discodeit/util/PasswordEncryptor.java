package com.sprint.mission.discodeit.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncryptor {
  public static String hashPassword(String plainPassword) {
    return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
  }

  public static Boolean checkPassword(String plainPassword, String hashedPassword) {
    return BCrypt.checkpw(plainPassword, hashedPassword);
  }
}
