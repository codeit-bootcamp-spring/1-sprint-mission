package com.sprint.mission.discodeit.common.validation;

import java.util.regex.Pattern;

public class ValidatorImpl implements Validator {

    private static final Pattern EMAIL_EXP = Pattern.compile("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");
    private static final Pattern NICKNAME_EXP = Pattern.compile("^.{1,31}$");
    private static final Pattern PASSWORD_EXP = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@!%*#?&])[A-Za-z\\d@!%*#?&]{8,}$");
    private static final Pattern PHONE_EXP = Pattern.compile("^\\d{2,3}-\\d{3,4}-\\d{4}$");
    private static final Pattern USERNAME_EXP = Pattern.compile("^(?!.*[.]{2})[a-zA-Z0-9](?:[a-zA-Z0-9_.]{0,30}[a-zA-Z0-9])?$");

    @Override
    public boolean emailValidator(String email) {
        return EMAIL_EXP.matcher(email).matches();
    }

    @Override
    public boolean nicknameValidator(String nickname) {
        return NICKNAME_EXP.matcher(nickname).matches();
    }

    @Override
    public boolean passwordValidator(String password) {
        return PASSWORD_EXP.matcher(password).matches();
    }

    @Override
    public boolean phoneNumValidator(String phoneNum) {
        return PHONE_EXP.matcher(phoneNum).matches();
    }

    @Override
    public boolean userNameValidator(String username) {
        return USERNAME_EXP.matcher(username).matches();
    }
}
