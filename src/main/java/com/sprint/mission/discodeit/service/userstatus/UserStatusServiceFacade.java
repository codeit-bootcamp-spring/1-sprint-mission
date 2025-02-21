package com.sprint.mission.discodeit.service.userstatus;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.dto.userstatus.UserStatusDeleteResponse;
import com.sprint.mission.discodeit.service.dto.userstatus.UserStatusRegisterResponse;
import com.sprint.mission.discodeit.service.dto.userstatus.UserStatusSearchResponse;
import com.sprint.mission.discodeit.service.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.service.dto.userstatus.UserStatusUpdateResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserStatusServiceFacade {

    private final UserStatusRegisterService registerService;
    private final UserStatusRepository userStatusRepository;
    private final UserStatusUpdateService updateService;
    private final UserStatusDeleteService deleteService;

    public UserStatusRegisterResponse registerUser() {

        UserStatus userStatus = registerService.registerUserStatus();

        return UserStatusRegisterResponse.from(userStatus);
    }

    public UserStatusSearchResponse searchUserById(UUID userStatusId) {

        UserStatus userStatusById =
            userStatusRepository.findUserStatusById(userStatusId);

        return UserStatusSearchResponse.from(userStatusById);
    }

    public List<UserStatus> searchAllUser() {

        return userStatusRepository.findAllUserStatus();
    }

    public UserStatusUpdateResponse update(UserStatusUpdateRequest request) {

        UserStatus updatedUserStatus = updateService.update(request);

        return UserStatusUpdateResponse.from(updatedUserStatus);
    }

    public UserStatusDeleteResponse deleteById(UUID userId) {

        UserStatus deleteUserStatusById = deleteService.deleteById(userId);

        return UserStatusDeleteResponse.from(deleteUserStatusById);
    }
}
