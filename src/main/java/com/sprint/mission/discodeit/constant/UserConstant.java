package com.sprint.mission.discodeit.constant;

public class UserConstant {
    public static final int USERNAME_MIN_LENGTH = 2;
    public static final int USERNAME_MAX_LENGTH = 100;
    public static final int PASSWORD_MIN_LENGTH = 5;
    public static final int PASSWORD_MAX_LENGTH = 100;
    public static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    public static final String DEFAULT_PROFILE_PICTURE_URL = "https://default-profile-pic.com";
    public static final String ERROR_USERNAME_LENGTH = "Username must be between " + USERNAME_MIN_LENGTH + " and " + USERNAME_MAX_LENGTH + " characters.";
    public static final String ERROR_PASSWORD_LENGTH = "Password must be between " + PASSWORD_MIN_LENGTH + " and " + PASSWORD_MAX_LENGTH + " characters.";
    public static final String ERROR_INVALID_EMAIL = "Invalid email format.";
}
