package com.sprint.mission.discodeit.domain.user.validation;

import com.sprint.mission.discodeit.domain.user.exception.NickNameInvalidException;
import com.sprint.mission.discodeit.global.error.ErrorCode;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

public class NicknameValidator {
    private static final int MAX_LENGTH = 32;
    private static final Set<String> FORBIDDEN_WORDS = Set.of(
            "admin", "moderator", "discord", "system", "root", "bot", "mod",
            "운영자", "관리자", "봇"
    );

    private static final String VALID_NICK_NAME_REGEX =
            "^(?!.*\\s)[\\p{L}\\p{N}\\p{P}\\p{S}\\p{So}]{1,32}$\n";
    private static final Pattern VALID_NICK_NAME_PATTERN = Pattern.compile(VALID_NICK_NAME_REGEX);

    public static void validate(String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            throw new NickNameInvalidException(ErrorCode.NICKNAME_REQUIRED, "");
        }

        if (value.length() > MAX_LENGTH) {
            throw new NickNameInvalidException(ErrorCode.INVALID_NICKNAME_LENGTH, value);
        }

        if (!VALID_NICK_NAME_PATTERN.matcher(value).matches()) {
            throw new NickNameInvalidException(ErrorCode.INVALID_NICKNAME_FORMAT, value);
        }

        String lowerCase = value.toLowerCase();
        for (String word : FORBIDDEN_WORDS) {
            if (lowerCase.contains(word)) {
                throw new NickNameInvalidException(ErrorCode.INVALID_NICKNAME_FORMAT, value);
            }
        }
    }
}
