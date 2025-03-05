package com.sprint.mission.discodeit.util.validation;

import lombok.Getter;

import java.util.regex.Pattern;

@Getter
public enum ValidatorExp {
    EMAIL("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$"),
    NICKNAME("^.{1,31}$"),
    PASSWORD("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@!%*#?&])[A-Za-z\\d@!%*#?&]{8,}$"),
    PHONE("^\\d{2,3}-\\d{3,4}-\\d{4}$"),
    USERNAME("^(?!.*[.]{2})[a-zA-Z0-9](?:[a-zA-Z0-9_.]{0,30}[a-zA-Z0-9])?$");

    private final String regex;
    private final Pattern pattern;

    ValidatorExp(String regex) {
        this.regex = regex;
        this.pattern = Pattern.compile(regex);
    }

    public boolean matches(String value) {
        return value != null && pattern.matcher(value).matches();
    }
}
