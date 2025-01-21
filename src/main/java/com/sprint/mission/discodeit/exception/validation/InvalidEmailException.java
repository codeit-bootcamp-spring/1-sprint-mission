package com.sprint.mission.discodeit.exception.validation;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String message) {
        super(message);
    }
}
