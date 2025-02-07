package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionText;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
@Component
@RequiredArgsConstructor
public class UserValidator {

    // 간단한 형태의 이메일 검증
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    // 비밀번호 검증: 최소 8자, 대문자, 소문자, 숫자, 특수문자 포함
    private static final String PASSWORD_REGEX = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z0-9@$!%*?&]{8,}";
    private static final int MIN_NAME_LENGTH = 2;

    private final FileUserRepository fileUserRepository;

    private boolean isValidEmail(String email) {
        if (email == null || !Pattern.matches(EMAIL_REGEX, email)) {
            throw new CustomException(ExceptionText.INVALID_EMAIL);
        }
        return true;
    }

    private boolean isValidPassword(String password) {
        if (password == null || !Pattern.matches(PASSWORD_REGEX, password)) {
            throw new CustomException(ExceptionText.INVALID_PASSWORD);
        }
        return true;
    }

    private boolean isValidName(String name) {
        if (name == null || name.length() < MIN_NAME_LENGTH) {
            throw new CustomException(ExceptionText.INVALID_NAME);
        }
        return true;
    }
    //같은 이름을 가진 유저가 존재하는지
    private boolean checkNameUnique(String name) {
        Map<UUID, User> users =fileUserRepository.findAll();
        if (users.values().stream().anyMatch(user -> user.getName().equals(name))) {
            throw new CustomException(ExceptionText.NAME_DUPLICATE);
        }
        return true;// 중복 이름 예외
    }
    //같은 이메일을 가진 유저가 존재하는지
    private boolean checkEmailUnique(String email){
        Map<UUID, User> users =fileUserRepository.findAll();
        if(users.values().stream().anyMatch(user -> user.getEmail().equals(email))){
            throw new CustomException(ExceptionText.EMAIL_DUPLICATE);
        }
        return true;
    }

    public void CheckUser(UUID userId){
        Map<UUID, User> users =fileUserRepository.findAll();
        if (!users.containsKey(userId)) {
            throw new CustomException(ExceptionText.USER_NOT_FOUND);
        }
    }

    public void validateUser(String name, String email, String password) {
        isValidName(name);
        isValidEmail(email);
        isValidPassword(password);
        checkNameUnique(name);
        checkEmailUnique(email);
    }
}
