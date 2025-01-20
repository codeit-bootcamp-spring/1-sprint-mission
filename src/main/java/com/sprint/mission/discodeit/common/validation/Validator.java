package com.sprint.mission.discodeit.common.validation;

public interface Validator {
    boolean emailValidator(String email);

    boolean nicknameValidator(String nickname);

    boolean passwordValidator(String password);

    boolean phoneNumValidator(String phoneNum);

    boolean userNameValidator(String username);
}
