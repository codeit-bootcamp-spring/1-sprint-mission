package com.sprint.mission.discodeit.validator;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public void validateUser(String username, String email, String password){
        validateUsername(username);
        validateEmail(email);
        validatePassword(password);
        checkDuplicateUser(username, email);
    }

    public void validateUpdateUser(UUID userId, String username, String email){
        validateUserId(userId);
        validateUsername(username);
        validateEmail(email);
        checkDuplicateUser(username, email);
    }

    public void validateUserId(UUID userId){
        User findUser = userRepository.findOne(userId);
        Optional.ofNullable(findUser)
                .orElseThrow(() -> new NoSuchElementException("해당 User 가 없습니다."));
    }

    public void validateUsername(String username){
        if (username == null || username.trim().isEmpty() ) {
            throw new IllegalArgumentException("입력 값은 null이거나 공백일 수 없습니다.");
        }
        if (!username.matches("^[a-zA-Z0-9_]{3,20}$")) {
            throw new IllegalArgumentException("only letters, numbers, and underscore 허용됩니다.");
        }
    }

    public void validateEmail(String email){
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("입력 값은 null이거나 공백일 수 없습니다.");
        }
        if (!email.matches("^[\\w.-]+@[\\w-]+\\.[a-zA-Z]{2,6}$")) {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }

    public void validatePassword(String password){
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("입력 값은 null이거나 공백일 수 없습니다.");
        }
    }

    public void checkDuplicateUser(String username, String email) {
        boolean present = userRepository.findAll().stream()
                .anyMatch(user -> user.getUsername().equals(username) || user.getEmail().equals(email));
        if (present) {
            throw new IllegalArgumentException("이름 혹은 이메일이 중복입니다.");
        }
    }

}
