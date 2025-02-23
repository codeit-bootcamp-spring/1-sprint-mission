package com.sprint.mission.discodeit.exception;

public class LoginFailedException extends RuntimeException{
    public LoginFailedException(String message) {
        super(message);
    }
}
