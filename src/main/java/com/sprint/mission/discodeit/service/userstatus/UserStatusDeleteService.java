package com.sprint.mission.discodeit.service.userstatus;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserStatusDeleteService {

    private final UserStatusRepository userStatusRepository;

    public UserStatus deleteById(UUID userId) {

        return userStatusRepository.deleteUserStatusById(userId);
    }
}
