package com.sprint.mission.discodeit.constant;

import java.util.UUID;

public enum StringConstant {
    EMAIL_FORMAT("UserName@DomainName.TopLevelDomain"),
    PHONE_NUMBER_FORMAT("000-0000-0000"),
    EMPTY_STRING(""),
    EMPTY_UUID(new UUID(0L, 0L).toString()),
    ;

    private final String value;

    StringConstant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
