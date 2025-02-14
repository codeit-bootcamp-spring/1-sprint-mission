package com.sprint.mission.discodeit.entity.security;

import org.springframework.stereotype.Service;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Service
public class Encryptor {
  public  String getSalt() {
    SecureRandom random = new SecureRandom();
    byte[] salt = new byte[20];
    random.nextBytes(salt);
    StringBuilder sb = new StringBuilder();
    for (byte s : salt) {
      sb.append(String.format("%02x", s));
    }
    return sb.toString();
  }
  
  public  String encryptPassword(String password, String salt) {
    String result = "";
    
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      
      System.out.println("암호화 적용 전: " + password);
      md.update((password + salt).getBytes());
      byte[] bytes = md.digest();
      
      StringBuilder sb = new StringBuilder();
      for (byte b : bytes) {
        sb.append(String.format("%02x", b));
      }
      result = sb.toString();
      System.out.println("암호화 적용 후 " + result);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Encryption algorithm not found", e);
    }
    return result;
  }
  
  public boolean verifyPassword(String inputPassword, String storedEncryptedPassword, String salt) {
    String encryptedInputPassword = encryptPassword(inputPassword, salt);
    return encryptedInputPassword.equals(storedEncryptedPassword);
  }
}
