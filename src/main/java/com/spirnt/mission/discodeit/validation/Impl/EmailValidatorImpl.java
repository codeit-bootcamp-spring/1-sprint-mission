package com.spirnt.mission.discodeit.validation.Impl;

import com.spirnt.mission.discodeit.validation.EmailValidator;

import java.util.Objects;
import java.util.regex.Pattern;

public class EmailValidatorImpl implements EmailValidator {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean isValid(String email) {
        if(Objects.isNull(email) || email.isEmpty()){
            return false;
        }else{
            return  EMAIL_PATTERN.matcher(email).matches();
        }

    }
}
