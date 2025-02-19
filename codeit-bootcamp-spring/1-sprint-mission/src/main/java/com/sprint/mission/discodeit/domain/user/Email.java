package com.sprint.mission.discodeit.domain.user;

import com.sprint.mission.discodeit.domain.user.exception.EmailInvalidException;
import com.sprint.mission.discodeit.global.error.ErrorCode;
import java.util.Objects;
import java.util.regex.Pattern;

public class Email {

    private final static String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z]{2,7})+$";
    private final static Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private final String value;

    public Email(String email) {
        valid(email);
        this.value = email;
    }

    public void valid(String email) {
        if (Objects.isNull(email) || email.isBlank()) {
            throw new EmailInvalidException(ErrorCode.EMAIL_REQUIRED, "");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new EmailInvalidException(ErrorCode.INVALID_EMAIL_FORMAT, email);
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Email email = (Email) o;
        return Objects.equals(value, email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "Email{" +
                "value='" + value + '\'' +
                '}';
    }
}
