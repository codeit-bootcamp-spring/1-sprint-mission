package com.spirnt.mission.discodeit.validation;

public interface Validator {
    boolean emailIsValid(String email);
    boolean phoneNumberIsValid(String phoneNumber);
}
