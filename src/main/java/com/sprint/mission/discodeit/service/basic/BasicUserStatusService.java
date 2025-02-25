package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.UserStatus;
import com.sprint.mission.discodeit.dto.userstatus.CreateUserStatusRequest;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.ServiceException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatus create(CreateUserStatusRequest request) {
        validUser(request.userID());

        userStatusRepository.findByUserId(request.userID())
                .ifPresent(userStatus -> {
                    throw new ServiceException(ErrorCode.ALREADY_EXIST_USERSTAUTS);
                });

        UserStatus userStatus = new UserStatus(request.userID(), request.lastActiveAt());
        userStatusRepository.save(userStatus);
        return userStatus;
    }

    @Override
    public UserStatus find(UUID id) {
        UserStatus userStatus = userStatusRepository.findById(id).orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_USERSTATUS));
        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus findByUserId(UUID userID) {
        UserStatus userStatus = userStatusRepository.findByUserId(userID).orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_USERSTATUS));
        return userStatusRepository.save(userStatus);
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    @Override
    public UserStatus updateByUserId(UUID userId) {
        validUser(userId);

        UserStatus userStatus = userStatusRepository.findByUserId(userId).orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_USERSTATUS));
        userStatus.update(Instant.now());
        userStatusRepository.save(userStatus);
        return userStatus;
    }

    @Override
    public void delete(UUID id) {
        userStatusRepository.deleteById(id);
    }

    private void validUser(UUID userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_USER));
    }
}
