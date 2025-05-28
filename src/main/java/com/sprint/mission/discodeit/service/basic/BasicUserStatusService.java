package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.request.UpdateUserStatusByIdRequest;
import com.sprint.mission.discodeit.dto.userStatus.request.UpdateUserStatusByUserIdRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.validator.UserStatusValidator;
import com.sprint.mission.discodeit.validator.UserValidator;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserStatusValidator userStatusValidator;
    private final UserValidator userValidator;
    private final UserStatusMapper userStatusMapper;

    @Override
    @Transactional
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
    @Transactional
    public UserStatus updateUserStatusById(
        UpdateUserStatusByIdRequest updateUserStatusByIdRequest) {
        UserStatus userStatus =
            userStatusValidator.validateUserStatusExistsById(
                updateUserStatusByIdRequest.userStatusId());

        userStatus.updateUserStatusInfo(Instant.now());

        return userStatus;
    }

    @Override
    @Transactional
    public UserStatusDto updateUserStatusByUserId(UUID userid,
        UpdateUserStatusByUserIdRequest updateUserStatusByUserIdRequest) {
        User user = userValidator.validateUserExistsByUserId(userid);
        UserStatus userStatus = userStatusValidator.validateUserStatusExistsByUser(user);

        userStatus.updateUserStatusInfo(updateUserStatusByUserIdRequest.newLastActiveAt());

        return userStatusMapper.toUserStatusDto(userStatus);
    }

    @Override
    public void deleteUserStatus(UUID userStatusId) {
        userStatusValidator.validateUserStatusExistsById(userStatusId);

        userStatusRepository.removeUserStatus(userStatusId);
    }
}
