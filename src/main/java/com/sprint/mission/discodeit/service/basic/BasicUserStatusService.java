package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.RestApiException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;  // 순환 참조 발생

    @Override
    public UserStatus create(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RestApiException(ErrorCode.USER_NOT_FOUND, "userId : " + userId));
        if (userStatusRepository.existsByUserId(userId)) {
            throw new DuplicateRequestException("UserStatus already exists");
        }
        UserStatus newUserStatus = UserStatus.createUserStatus(userId);
        log.info("Create UserStatus: {}" , newUserStatus);
        return userStatusRepository.save(newUserStatus);
    }

    @Override
    public UserStatus findById(UUID id) {
        return userStatusRepository.findById(id)
                .orElseThrow(() -> new RestApiException(ErrorCode.USER_STATUS_NOT_FOUND, "id : " + id));
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        return userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new RestApiException(ErrorCode.USER_STATUS_NOT_FOUND, "userId : " + userId));
    }

    @Override
    public UserStatus updateByUserId(UUID userId) {
        UserStatus userStatus = findByUserId(userId);
        userStatus.updateStatus();
        return userStatusRepository.save(userStatus);
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    @Override
    public void deleteById(UUID id) {
        userStatusRepository.deleteById(id);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        userStatusRepository.deleteByUserId(userId);
    }
}
