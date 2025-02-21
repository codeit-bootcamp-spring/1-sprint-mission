package com.sprint.mission.discodeit.service.userstatus;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.service.dto.userstatus.UserStatusUpdateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserStatusUpdateService {

    private final UserStatusRepository userStatusRepository;

    public UserStatus update(UserStatusUpdateRequest request) {

        UserStatus userStatusToUpdate = UserStatus.createUserStatus(
            request.userId(),
            request.createAt(),
            request.updateAt(),
            request.lastAccess()
        );

        return userStatusRepository.updateUserStatus(request.userId(), userStatusToUpdate);
    }
}
