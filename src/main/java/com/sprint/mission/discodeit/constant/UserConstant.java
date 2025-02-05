package com.sprint.mission.discodeit.constant;

public class UserConstant {
  public static final int USERNAME_MIN_LENGTH = 2;
  public static final int USERNAME_MAX_LENGTH = 100;
  public static final int PASSWORD_MIN_LENGTH = 5;
  public static final int PASSWORD_MAX_LENGTH = 100;
  public static final String PHONE_REGEX = "^(01[016789]-?\\d{3,4}-?\\d{4})$|^(\\d{2,3}-?\\d{3,4}-?\\d{4})$";
  public static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
  public static final String DEFAULT_PROFILE_PICTURE_URL = "https://default-profile-pic.com";
  public static final String ERROR_USERNAME_LENGTH = "Username must be between " + USERNAME_MIN_LENGTH + " and " + USERNAME_MAX_LENGTH + " characters.";
  public static final String ERROR_PASSWORD_LENGTH = "Password must be between " + PASSWORD_MIN_LENGTH + " and " + PASSWORD_MAX_LENGTH + " characters.";
  public static final String ERROR_INVALID_EMAIL = "Invalid email format.";
  public static final String ERROR_INVALID_PHONE = "허용되지 않는 전화번호입니다.";
  public static final String NO_MATCHING_USER = "사용자를 찾을 수 없습니다.";
  public static final String PASSWORD_MATCH_ERROR = "비밀번호가 일치하지 않습니다.";
  public static final String DUPLICATE_EMAIL = "중복된 이메일 입니다.";
  public static final String DUPLICATE_PHONE = "중복된 번호입니다.";
  public static final String DUPLICATE_NICKNAME = "중복된 nickname 입니다.";
}
