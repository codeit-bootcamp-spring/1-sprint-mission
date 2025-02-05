package com.sprint.mission.discodeit.domain.user.validation;

import java.util.regex.Pattern;
import org.springframework.util.Assert;

public class EmailValidator {

    private final static String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";
    private final static Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);


    public static void valid(String email) {
        Assert.notNull(email, "이메일은 필수입니다.");
        if (email.isBlank()) {
            throw new IllegalArgumentException("이메일은 필수입니다.");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("이메일 형식이 틀렸습니다 : 입력 = " + email);
        }
    }
}
