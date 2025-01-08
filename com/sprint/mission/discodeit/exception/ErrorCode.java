package sprint.mission.discodeit.exception;

import static sprint.mission.discodeit.constant.Constants.*;

public enum ErrorCode {
    /*
    * 000 ~ 009 : Common  Error
    * 010 ~ 019 : User    Error
    * 020 ~ 029 : Message Error
    * 030 ~ 039 : Channel Error
    */
    ERROR_000("Invalid Id", "Id must not be '" + EMPTY_UUID.getAsString() + "'"),
    ERROR_001("Invalid Time", "Time must not be " + EMPTY_TIME.getAsString()),
    ERROR_010("Invalid Name Format", "Name must be " + MAX_NAME_LENGTH.getAsString() + " characters or less"),
    ERROR_011("Invalid Email Format", "Email must be '" + EMAIL_FORMAT.getAsString() + "'"),
    ERROR_012("Invalid PhoneNumber Format", "PhoneNumber must be '" + PHONE_NUMBER_FORMAT.getAsString() + "'"),
    ERROR_020("Invalid Content Format", "Content must be " + MAX_CONTENT_LENGTH.getAsString() + " characters or less"),
    ERROR_030("Invalid Name Format", "Name must be " + MAX_NAME_LENGTH.getAsString() + " characters or less");

    private final String definition;
    private final String description;

    ErrorCode(String definition, String description) {
        this.definition  = definition;
        this.description = description;
    }

    public String getDefinition() {
        return definition;
    }
    public String getDescription() {
        return description;
    }
}
