package com.sprint.mission.discodeit.constant;

import java.time.Instant;
import lombok.Getter;

@Getter
public enum IntegerConstant {
    NOT_FOUND(-1),
    MAX_NAME_LENGTH(10),
    PHONE_NUMBER_LENGTH(11),
    MAX_CONTENT_LENGTH(20),
    ;

    private final int value;

    IntegerConstant(int value) {
        this.value = value;
    }

}

