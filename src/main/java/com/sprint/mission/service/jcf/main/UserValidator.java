package com.sprint.mission.service.jcf.main;

import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.entity.main.User;
import java.util.List;

public class UserValidator {
    public void isDuplicateNameEmail(List<User> allUser, String username, String email) {
        boolean isDuplicateName = allUser.stream()
                .anyMatch(usr -> username.equals(usr.getUsername()));

        if (isDuplicateName) throw new CustomException(ErrorCode.ALREADY_EXIST_NAME);

        boolean isDuplicateEmail = allUser.stream()
                .anyMatch(usr -> email.equals(usr.getEmail()));
        if (isDuplicateEmail) throw new CustomException(ErrorCode.ALREADY_EXIST_EMAIL);
    }



}
