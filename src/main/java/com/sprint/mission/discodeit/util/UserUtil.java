package com.sprint.mission.discodeit.util;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.notfound.NotfoundIdException;
import com.sprint.mission.discodeit.exception.validation.user.InvalidEmailException;
import com.sprint.mission.discodeit.exception.validation.user.InvalidPasswordException;
import com.sprint.mission.discodeit.exception.validation.user.InvalidPhoneNumberException;
import com.sprint.mission.discodeit.exception.validation.user.InvalidUsernameException;

import static com.sprint.mission.discodeit.util.RegexUtil.*;

public class UserUtil {
    public static void validUserId(User user) {
        if (user.getId() == null) {
            throw new NotfoundIdException("유효하지 않은 id입니다.");
        }
    }

    public static void validUsername(String username) {
        if (username == null || username.isEmpty() || username.length() > 32) {
            throw new InvalidUsernameException(username + "은 유효하지 않은 이름명입니다.");
        }
    }

    public static void validEmail(String email) {
        if (!email.matches(EMAIL_REGEX)) {
            throw new InvalidEmailException(email + "은 유효하지 않은 이메일입니다.");
        }
    }

    public static void validPhoneNumber(String phoneNumber) {
        if (!phoneNumber.matches(PHONE_NUMBER_REGEX)) {
            throw new InvalidPhoneNumberException(phoneNumber + "은 유효하지 않은 전화번호입니다.");
        }
    }

    public static void validPassword(String password) {
        if (!password.matches(PASSWORD_REGEX)) {
            throw new InvalidPasswordException("유효하지 않은 비밀번호입니다.");
        }
    }

    public static void checkValid(String username, String email, String phoneNumber, String password) {
        validUsername(username);
        validEmail(email);
        validPhoneNumber(phoneNumber);
        validPassword(password);
    }

}
