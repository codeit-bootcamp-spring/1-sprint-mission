package com.sprint.mission.discodeit.domain.user;

import com.sprint.mission.discodeit.domain.user.exception.NickNameInvalidException;
import com.sprint.mission.discodeit.global.error.ErrorCode;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(of = {"value"})
@ToString(of = {"value"})
public class Nickname {

    private static final int MAX_LENGTH = 32;
    private static final Set<String> FORBIDDEN_WORDS = Set.of(
            "admin", "moderator", "discord", "system", "root", "bot", "mod",
            "운영자", "관리자", "봇"
    );
    private static final String VALID_NICK_NAME_REGEX =
            "^(?!.*\\s)[\\p{L}\\p{N}\\p{P}\\p{S}\\p{So}]{1,32}$";
    private static final Pattern VALID_NICK_NAME_PATTERN = Pattern.compile(VALID_NICK_NAME_REGEX);

    private final String value;

    public Nickname(String value) {
        validate(value);
        this.value = value;
    }

    public void validate(String value) {
        throwIsNull(value);
        throwOverLength(value);
        throwInvalidPattern(value);
        throwContainForbiddenWord(value);
    }

    private void throwIsNull(String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            throw new NickNameInvalidException(ErrorCode.NICKNAME_REQUIRED, "");
        }
    }

    private void throwOverLength(String value) {
        if (value.length() > MAX_LENGTH) {
            throw new NickNameInvalidException(ErrorCode.INVALID_NICKNAME_LENGTH, value);
        }
    }

    private void throwInvalidPattern(String value) {
        if (!VALID_NICK_NAME_PATTERN.matcher(value).matches()) {
            throw new NickNameInvalidException(ErrorCode.INVALID_NICKNAME_FORMAT, value);
        }
    }

    private void throwContainForbiddenWord(String value) {
        String lowerCase = value.toLowerCase();
        for (String word : FORBIDDEN_WORDS) {
            if (lowerCase.contains(word)) {
                throw new NickNameInvalidException(ErrorCode.INVALID_NICKNAME_FORMAT, value);
            }
        }
    }
}
