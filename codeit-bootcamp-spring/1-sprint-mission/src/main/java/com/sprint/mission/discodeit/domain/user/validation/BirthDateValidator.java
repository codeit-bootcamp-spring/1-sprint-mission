package com.sprint.mission.discodeit.domain.user.validation;

import com.sprint.mission.discodeit.domain.user.exception.BirthDateInvalidException;
import com.sprint.mission.discodeit.global.error.ErrorCode;
import java.time.LocalDate;
import java.util.Objects;

public class BirthDateValidator {
    private static final int MIN_AGE_RESTRICT = 13;

    public static void validate(LocalDate birthDate) {
        if (Objects.isNull(birthDate)) {
            throw new BirthDateInvalidException(ErrorCode.BIRTHDATE_REQUIRED, "");
        }

        LocalDate minimumAllowedYear = LocalDate.now().minusYears(MIN_AGE_RESTRICT);
        if (!birthDate.isBefore(minimumAllowedYear)) {
            throw new BirthDateInvalidException(ErrorCode.UNDERAGE_SIGNUP_REGISTERED, birthDate.toString());
        }
    }
}
