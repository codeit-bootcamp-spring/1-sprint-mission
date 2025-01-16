package com.sprint.mission.discodeit.constant;

public enum IntegerConstant {
    NOT_FOUND(-1),
    EMPTY_TIME(0),
    MAX_NAME_LENGTH(10),
    PHONE_NUMBER_LENGTH(11),
    MAX_CONTENT_LENGTH(20),

    ;

    private final int value;

    IntegerConstant(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

