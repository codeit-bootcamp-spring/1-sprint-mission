package com.sprint.mission.discodeit.domain.user.validation;

import com.sprint.mission.discodeit.domain.user.exception.EmailInvalidException;
import com.sprint.mission.discodeit.global.error.ErrorCode;
import java.util.Objects;
import java.util.regex.Pattern;

public class EmailValidator {

    private final static String EMAIL_REGEX =
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z]{2,7})+$";

    private final static Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);


    public static void valid(String email) {
        if (Objects.isNull(email)) {
            throw new EmailInvalidException(ErrorCode.EMAIL_REQUIRED, "");
        }
        if (email.isBlank() || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new EmailInvalidException(ErrorCode.INVALID_EMAIL_FORMAT, email);
        }

    }
}
