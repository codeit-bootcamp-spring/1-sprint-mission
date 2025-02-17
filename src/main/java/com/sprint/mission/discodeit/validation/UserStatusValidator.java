package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.exception.duplication.DuplicateResourceException;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserStatusValidator {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    public void validateUserStatus(UUID userStatusId){
        if (userStatusRepository.existsById(userStatusId)){
            throw new ResourceNotFoundException("User status not found: " + userStatusId);
        }
    }

    // User가 존재하지 않을 경우
    public void validateUser(UUID userId){
        if (!userRepository.existsById(userId)){
            throw new ResourceNotFoundException("User not found: " + userId);
        }
    }

}
