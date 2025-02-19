package com.sprint.mission.discodeit.validator;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.BadRequestException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.NotFoundException;
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

    public void validateUpdateUser(UUID userId, String username, String email, String password){
        validateUserId(userId);
        validateUsername(username);
        validateEmail(email);
        validatePassword(password);
        checkDuplicateUser(username, email);
    }

    public void validateUserId(UUID userId){
        User findUser = userRepository.findOne(userId);
        Optional.ofNullable(findUser)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    public void validateUsername(String username){
        if (username == null || username.trim().isEmpty() ) {
            throw new BadRequestException(ErrorCode.INPUT_VALUE_INVALID);
        }
        if (!username.matches("^[a-zA-Z0-9_]{3,20}$")) {
            throw new BadRequestException(ErrorCode.INPUT_VALUE_MISMATCH);
        }
    }

    public void validateEmail(String email){
        if (email == null || email.trim().isEmpty()) {
            throw new BadRequestException(ErrorCode.INPUT_VALUE_INVALID);
        }
        if (!email.matches("^[\\w.-]+@[\\w-]+\\.[a-zA-Z]{2,6}$")) {
            throw new BadRequestException(ErrorCode.INPUT_VALUE_MISMATCH);
        }
    }

    public void validatePassword(String password){
        if (password == null || password.trim().isEmpty()) {
            throw new BadRequestException(ErrorCode.INPUT_VALUE_INVALID);
        }
    }

    public void checkDuplicateUser(String username, String email) {
        boolean present = userRepository.findAll().stream()
                .anyMatch(user -> user.getUsername().equals(username) || user.getEmail().equals(email));
        if (present) {
            throw new BadRequestException(ErrorCode.USER_DUPLICATE);
        }
    }

}
