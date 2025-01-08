package com.sprint.mission.discodeit.entity.user;

import static com.sprint.mission.discodeit.entity.user.UserName.UserErrorMessage.NAME_LENGTH_ERROR_MESSAGE;
import static com.sprint.mission.discodeit.entity.user.UserName.UserErrorMessage.NAME_NULL_MESSAGE;

import java.util.Objects;

public class UserName {

    public static final int NAME_MIN_LENGTH = 3;
    public static final int NAME_MAX_LENGTH = 20;

    private final String name;

    public UserName(String name) {
        validUsernameBiggerThan3AndLessThen10(name);
        this.name = name;
    }

    public UserName changeName(String name) {
        return new UserName(name);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserName userName = (UserName) o;
        return Objects.equals(name, userName.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    private void validUsernameBiggerThan3AndLessThen10(String name) {

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException(NAME_NULL_MESSAGE.message);
        }

        if (name.trim().length() < NAME_MIN_LENGTH || name.trim().length() > NAME_MAX_LENGTH) {
            throw new IllegalArgumentException(NAME_LENGTH_ERROR_MESSAGE.message);
        }
    }

    static enum UserErrorMessage {
        NAME_NULL_MESSAGE("Username cannot be null or empty"),
        NAME_LENGTH_ERROR_MESSAGE("Username Length Error"),
        ;
        private final String message;

        UserErrorMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
