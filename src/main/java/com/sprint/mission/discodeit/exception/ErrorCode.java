package com.sprint.mission.discodeit.exception;

import static com.sprint.mission.discodeit.constant.IntegerConstant.*;
import static com.sprint.mission.discodeit.constant.StringConstant.*;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INVALID_ID_FORMAT("Id must not be '" + EMPTY_UUID.getValue() + "'"),
    INVALID_TIME_FORMAT("Time must not be " + EMPTY_TIME.getValue()),
    INVALID_NAME_FORMAT("Name must be " + MAX_NAME_LENGTH.getValue() + " characters or less"),
    INVALID_EMAIL_FORMAT("Email must be '" + EMAIL_FORMAT.getValue() + "'"),
    INVALID_PHONE_NUMBER_FORMAT("PhoneNumber must be '" + PHONE_NUMBER_FORMAT.getValue() + "'"),
    INVALID_CONTENT_FORMAT("Content must be " + MAX_CONTENT_LENGTH.getValue() + " characters or less"),
    ;

    private final String description;

    ErrorCode(String description) {
        this.description = description;
    }

}
