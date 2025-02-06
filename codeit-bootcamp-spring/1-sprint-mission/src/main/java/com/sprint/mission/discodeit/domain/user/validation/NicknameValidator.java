package com.sprint.mission.discodeit.domain.user.validation;

import com.sprint.mission.discodeit.domain.user.exception.NickNameInvalidException;
import com.sprint.mission.discodeit.global.error.ErrorCode;
import java.util.Objects;
import java.util.Set;

public class NicknameValidator {
    private static final int MAX_LENGTH = 32;
    private static final Set<String> FORBIDDEN_WORDS = Set.of(
            "admin", "moderator", "discord", "system", "root", "bot", "mod",
            "운영자", "관리자", "봇"
    );

    public static void validate(String value) {
        if (Objects.isNull(value)) {
            throw new NickNameInvalidException(ErrorCode.NICKNAME_REQUIRED, "");
        }

        if (value.isBlank() || value.length() > MAX_LENGTH) {
            throw new NickNameInvalidException(ErrorCode.INVALID_NICKNAME_LENGTH, value);
        }

        String lowerCase = value.toLowerCase();
        for (String word : FORBIDDEN_WORDS) {
            if (lowerCase.contains(word)) {
                throw new NickNameInvalidException(ErrorCode.INVALID_NICKNAME_FORMAT, value);
            }
        }
    }
}
