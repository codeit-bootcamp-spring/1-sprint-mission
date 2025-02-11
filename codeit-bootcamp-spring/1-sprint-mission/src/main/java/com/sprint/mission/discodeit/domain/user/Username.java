package com.sprint.mission.discodeit.domain.user;

import com.sprint.mission.discodeit.domain.user.exception.UserNameInvalidException;
import com.sprint.mission.discodeit.global.error.ErrorCode;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

public class Username {

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 32;
    private static final String VALID_USER_NAME_REGEX = "^(?!.*\\.\\.)(?![.])(?!.*[.]$)[a-zA-Z0-9_.]{2,32}$";
    private static final Pattern VALID_USER_NAME_PATTERN = Pattern.compile(VALID_USER_NAME_REGEX);
    private static final Set<String> FORBIDDEN_WORD =
            Set.of(
                    "admin", "moderator", "discord", "system", "root", "bot", "mod",
                    "운영자", "관리자", "봇"
            );

    private final String value;

    public Username(String value) {
        validate(value);
        this.value = value.toLowerCase();
    }

    public void validate(String username) {
        throwIsNull(username);
        throwInvalidLength(username);
        throwInvalidPattern(username);
        throwContainForbiddenWord(username);
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
        Username username = (Username) o;
        return Objects.equals(value, username.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "Username{" +
                "value='" + value + '\'' +
                '}';
    }

    private void throwIsNull(String username) {
        if (Objects.isNull(username) || username.isBlank()) {
            throw new UserNameInvalidException(ErrorCode.USERNAME_REQUIRED, "");
        }
    }

    private void throwInvalidLength(String username) {
        if (username.length() > MAX_LENGTH || username.length() < MIN_LENGTH) {
            throw new UserNameInvalidException(ErrorCode.INVALID_USERNAME_LENGTH, username);
        }
    }

    private void throwInvalidPattern(String username) {
        if (!VALID_USER_NAME_PATTERN.matcher(username).matches()) {
            throw new UserNameInvalidException(ErrorCode.INVALID_USERNAME_FORMAT, username);
        }
    }

    private void throwContainForbiddenWord(String username) {
        String lowerUsername = username.toLowerCase();
        for (String word : FORBIDDEN_WORD) {
            if (lowerUsername.contains(word)) {
                throw new UserNameInvalidException(ErrorCode.INVALID_USERNAME_FORMAT, username);
            }
        }
    }
}
