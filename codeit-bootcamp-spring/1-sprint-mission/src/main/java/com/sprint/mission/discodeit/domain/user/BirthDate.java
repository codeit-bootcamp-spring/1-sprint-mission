package com.sprint.mission.discodeit.domain.user;

import java.time.LocalDate;

public class BirthDate {

    private final LocalDate value;

    public BirthDate(int year, int month, int day) {
        this.value = LocalDate.of(year, month, day);
    }

    public LocalDate getValue() {
        return value;
    }
}
