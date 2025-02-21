package com.sprint.mission.discodeit.service.userstatus;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserStatusRegisterService {

    private final UserStatusRepository userStatusRepository;

    public UserStatus registerUserStatus(UUID userId) {

        UserStatus userStatusToCreate = UserStatus.createUserStatus(userId);

        return userStatusRepository.createUserStatus(userStatusToCreate);
    }
}
