package com.sprint.mission.discodeit.domain.user;

import com.sprint.mission.discodeit.domain.user.exception.BirthDateInvalidException;
import com.sprint.mission.discodeit.global.error.ErrorCode;
import java.time.LocalDate;
import java.util.Objects;

public class BirthDate {
    private static final int MIN_AGE_RESTRICT = 13;

    private final LocalDate value;

    public BirthDate(LocalDate value) {
        validate(value);
        this.value = value;
    }

    public static BirthDate of(int year, int month, int day) {
        return new BirthDate(LocalDate.of(year, month, day));
    }

    public void validate(LocalDate birthDate) {
        if (Objects.isNull(birthDate)) {
            throw new BirthDateInvalidException(ErrorCode.BIRTHDATE_REQUIRED, "");
        }
        LocalDate minimumAllowedYear = LocalDate.now().minusYears(MIN_AGE_RESTRICT);
        if (!birthDate.isBefore(minimumAllowedYear)) {
            throw new BirthDateInvalidException(ErrorCode.UNDERAGE_SIGNUP_REGISTERED, birthDate.toString());
        }
    }

    public LocalDate getValue() {
        return value;
    }
}
