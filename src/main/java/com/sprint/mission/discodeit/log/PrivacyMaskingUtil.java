package com.sprint.mission.discodeit.log;

public class PrivacyMaskingUtil {

  // 이메일 마스킹 (앞글자 4자리까지만 유지)
  public static String maskEmail(String email) {
    return email.replaceAll("(^.{4}).*(@.*)", "$1****$2");
  }

  public static String maskPassword(String password) {
    return password.replaceAll("^(.{2})(.{4})(.*)$", "$1****$3");
  }

}
