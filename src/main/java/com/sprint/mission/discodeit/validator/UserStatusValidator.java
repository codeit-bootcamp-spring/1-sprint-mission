package com.sprint.mission.discodeit.validator;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserStatusValidator {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    public void validateUserStatus(UUID userId){
        validateUserId(userId);
        checkDuplicateUserStatus(userId);
    }

    public void validateUserId(UUID userId){
        User findUser = userRepository.findOne(userId);
        Optional.ofNullable(findUser)
                .orElseThrow(() -> new NoSuchElementException("해당 User 가 없습니다."));
    }

    private void checkDuplicateUserStatus(UUID userId) {
        if (userStatusRepository.findByUserId(userId).isPresent()) {
            throw new IllegalArgumentException("중복된 UserStatus 가 존재합니다. Userid: " + userId);
        }

    }

}
