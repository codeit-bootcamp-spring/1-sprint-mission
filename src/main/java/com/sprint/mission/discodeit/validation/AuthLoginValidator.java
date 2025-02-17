package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.exception.validation.InvalidResourceException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthLoginValidator {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;


    // 헤딩 유저가 존재하는지
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Wrong password or user name."));
    }

    // UserStatus가 존재하는지
    public UserStatus findUserStatusByUserId(UUID userId) {
        return userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Status not found."));
    }

    // 패스워드가 일치하는지
    public void validatePassword(User user, String password) {
        if (!user.getPassword().equals(password)){
            throw new InvalidResourceException("Wrong password or user name.");
        }
    }
}
