package com.sprint.mission.discodeit.util;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.exception.duplication.DuplicateResourceException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserStatusUtil {
    private final UserStatusRepository userStatusRepository;

    public void validateForCreation(UserStatusCreateRequest request){
        validateDuplicateUserId(request.userId());
    }

    // 이미 UserStatus가 존재할 경우
    public void validateDuplicateUserId(UUID userId){
        if (userStatusRepository.existsByUserId(userId)){
            throw new DuplicateResourceException("UserStatus already exists.");
        }
    }


}
