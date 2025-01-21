package com.sprint.mission.discodeit.validation.Impl;

import com.sprint.mission.discodeit.validation.Validator;

import java.util.Objects;
import java.util.regex.Pattern;

public class ValidatorImpl implements Validator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern PHONE_REGEX = Pattern.compile("^(01[0-9])-(\\d{3,4})-(\\d{4})$", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean isValidEmail(String email) {
        if(Objects.isNull(email) || email.isEmpty()){
            return false;
        }else{
            return  EMAIL_PATTERN.matcher(email).matches();
        }
    }

    @Override
    public boolean isValidPhoneNumber(String phoneNumber){
        if(Objects.isNull(phoneNumber) || phoneNumber.isEmpty()){
            return false;
        }else{
            return  PHONE_REGEX.matcher(phoneNumber).matches();
        }
    }
}
