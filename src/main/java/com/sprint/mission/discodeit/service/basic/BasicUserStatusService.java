package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.request.UpdateUserStatusByIdRequest;
import com.sprint.mission.discodeit.dto.userStatus.request.UpdateUserStatusByUserIdRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.validator.UserStatusValidator;
import com.sprint.mission.discodeit.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserStatusValidator userStatusValidator;
    private final UserValidator userValidator;

    @Override
    public UserStatus createUserStatus(UUID userId) {
        User user = userValidator.validateUserExistsByUserId(userId);

        userStatusValidator.validateDuplicateByUser(user);

        return userStatusRepository.saveUserStatus(UserStatus.of(user));
    }

    @Override
    public UserStatus findUserStatusById(UUID userStatusId) {
        return userStatusValidator.validateUserStatusExistsById(userStatusId);
    }

    @Override
    public List<UserStatus> findAllUserStatuses() {
        return userStatusRepository.findAllUserStatuses();
    }

    @Override
    public UserStatus updateUserStatusById(UpdateUserStatusByIdRequest updateUserStatusByIdRequest) {
        UserStatus userStatus =
                userStatusValidator.validateUserStatusExistsById(updateUserStatusByIdRequest.userStatusId());

        userStatus.updateUserStatusInfo(updateUserStatusByIdRequest.isOnline());

        return userStatusRepository.saveUserStatus(userStatus);
    }

    @Override
    public UserStatus updateUserStatusByUserId(UUID userid,
                                               UpdateUserStatusByUserIdRequest updateUserStatusByUserIdRequest) {
        User user = userValidator.validateUserExistsByUserId(userid);
        UserStatus userStatus = userStatusValidator.validateUserStatusExistsByUser(user);

        userStatus.updateUserStatusInfo(updateUserStatusByUserIdRequest.isOnline());

        return userStatusRepository.saveUserStatus(userStatus);
    }

    @Override
    public void deleteUserStatus(UUID userStatusId) {
        userStatusValidator.validateUserStatusExistsById(userStatusId);

        userStatusRepository.removeUserStatus(userStatusId);
    }
}
