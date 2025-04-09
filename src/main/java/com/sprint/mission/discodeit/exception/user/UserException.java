package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class UserException extends DiscodeitException {
    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public static class UserNotFoundException extends UserException {
        public UserNotFoundException() {
            super(ErrorCode.USER_NOT_FOUND);
        }

        public UserNotFoundException(String message) {
            super(ErrorCode.USER_NOT_FOUND, message);
        }
    }

    public static class DuplicateUserException extends UserException {
        public DuplicateUserException() {
            super(ErrorCode.DUPLICATE_USER);
        }

        public DuplicateUserException(String message) {
            super(ErrorCode.DUPLICATE_USER, message);
        }
    }

    public static class DuplicateEmailException extends UserException {
        public DuplicateEmailException() {
            super(ErrorCode.DUPLICATE_EMAIL);
        }

        public DuplicateEmailException(String message) {
            super(ErrorCode.DUPLICATE_EMAIL, message);
        }
    }

    public static class DuplicateUsernameException extends UserException {
        public DuplicateUsernameException() {
            super(ErrorCode.DUPLICATE_USERNAME);
        }

        public DuplicateUsernameException(String message) {
            super(ErrorCode.DUPLICATE_USERNAME, message);
        }
    }
} 