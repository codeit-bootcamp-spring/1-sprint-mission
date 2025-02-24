package com.sprint.mission.discodeit.study;

import com.sprint.mission.discodeit.dto.userStatus.request.UpdateUserStatusByIdRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.validator.UserStatusValidator;
import com.sprint.mission.discodeit.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TempUserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserStatusValidator userStatusValidator;
    private final UserValidator userValidator;

    public UserStatus createUserStatus(UUID userId) {
        User user = userValidator.validateUserExistsByUserId(userId);

        userStatusValidator.validateDuplicateByUser(user);

        return userStatusRepository.saveUserStatus(UserStatus.of(user));
    }

    public UserStatus findUserStatusById(UUID userStatusId) {
        return userStatusValidator.validateUserStatusExistsById(userStatusId);
    }

    public UserStatus findUserStatusByUser(User user) {
        return userStatusValidator.validateUserStatusExistsByUser(user);
    }

    public List<UserStatus> findAllUserStatuses() {
        return userStatusRepository.findAllUserStatuses();
    }

    public UserStatus updateUserStatusById(UpdateUserStatusByIdRequest updateUserStatusByIdRequest) {
        UserStatus userStatus =
                userStatusValidator.validateUserStatusExistsById(updateUserStatusByIdRequest.userStatusId());

        userStatus.updateUserStatusInfo(updateUserStatusByIdRequest.isOnline());

        return userStatusRepository.saveUserStatus(userStatus);
    }

//    public UserStatus updateUserStatusByUserId(UpdateUserStatusByUserIdRequest updateUserStatusByUserIdRequest) {
//        User user = userValidator.validateUserExistsByUserId(updateUserStatusByUserIdRequest.userId());
//        UserStatus userStatus = userStatusValidator.validateUserStatusExistsByUser(user);
//
//        userStatus.updateUserStatusInfo(updateUserStatusByUserIdRequest.isOnline());
//
//        return userStatusRepository.saveUserStatus(userStatus);
//    }

    public void deleteUserStatus(UUID userStatusId) {
        userStatusValidator.validateUserStatusExistsById(userStatusId);

        userStatusRepository.removeUserStatus(userStatusId);
    }
}
