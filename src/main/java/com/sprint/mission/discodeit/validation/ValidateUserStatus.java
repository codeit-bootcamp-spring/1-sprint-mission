package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.exception.duplication.DuplicateResourceException;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ValidateUserStatus {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    public void validateUserStatus(UUID userId){
        validateUser(userId);
        validateDuplicateUserId(userId);
    }

    // 이미 UserStatus가 존재할 경우
    public void validateDuplicateUserId(UUID userId){
        if (userStatusRepository.existsByUserId(userId)){
            throw new DuplicateResourceException("UserStatus already exists.");
        }
    }

    // User가 존재하지 않을 경우
    public void validateUser(UUID userId){
        if (userRepository.findByUserId(userId).isEmpty()){
            throw new ResourceNotFoundException("User not found.");
        }
    }

}
