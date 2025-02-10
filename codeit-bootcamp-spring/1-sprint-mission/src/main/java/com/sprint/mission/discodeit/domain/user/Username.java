package com.sprint.mission.discodeit.domain.user;

import com.sprint.mission.discodeit.domain.user.exception.UserNameInvalidException;
import com.sprint.mission.discodeit.global.error.ErrorCode;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(of = "value")
@ToString(of = "value")
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

    public void validate(final String username) {
        if (Objects.isNull(username) || username.isBlank()) {
            throw new UserNameInvalidException(ErrorCode.USERNAME_REQUIRED, "");
        }

        if (username.length() > MAX_LENGTH || username.length() < MIN_LENGTH) {
            throw new UserNameInvalidException(ErrorCode.INVALID_USERNAME_LENGTH, username);
        }

        if (!VALID_USER_NAME_PATTERN.matcher(username).matches()) {
            throw new UserNameInvalidException(ErrorCode.INVALID_USERNAME_FORMAT, username);
        }

        String lowerUsername = username.toLowerCase();
        for (String word : FORBIDDEN_WORD) {
            if (lowerUsername.contains(word)) {
                throw new UserNameInvalidException(ErrorCode.INVALID_USERNAME_FORMAT, username);
            }
        }
    }
}
