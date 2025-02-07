package com.sprint.mission.discodeit.domain.user;

import com.sprint.mission.discodeit.domain.user.validation.BirthDateValidator;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class BirthDate {

    private final LocalDate value;

    public BirthDate(LocalDate value) {
        BirthDateValidator.validate(value);
        this.value = value;
    }

    public static BirthDate of(int year, int month, int day) {
        return new BirthDate(LocalDate.of(year, month, day));
    }

}
