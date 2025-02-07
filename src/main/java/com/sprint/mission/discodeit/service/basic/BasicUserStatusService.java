package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.util.UserStatusUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;
    private final UserStatusUtil userStatusUtil;

    @Override
    public UserStatusDto create(UserStatusCreateRequest request) {

        userStatusUtil.validateForCreation(request);
        UserStatus userStatus = new UserStatus(request.userId(), Instant.now());
        userStatusRepository.save(userStatus);
        return changeToDto(userStatus);
    }

    @Override
    public UserStatusDto findByUserId(UUID userId) {
        Optional<UserStatus> userStatus = userStatusRepository.findByUserId(userId);
        if (userStatus.isEmpty()) {
            throw new ResourceNotFoundException("UserStatus not found.");
        }
        return changeToDto(userStatus.get());
    }

    @Override
    public List<UserStatusDto> findAll() {
        List<UserStatus> userStatus = userStatusRepository.findAll();
        return userStatus.stream().map(this::changeToDto).collect(Collectors.toList());
    }

    @Override
    public UserStatusDto update(UserStatusUpdateRequest request) {
        Optional<UserStatus> userStatus = userStatusRepository.findByUserId(request.userId());
        if (userStatus.isEmpty()) {
            throw new NoSuchElementException("UserStatus not found.");
        }
        userStatus.get().update(request.lastLoginTime());
        userStatusRepository.save(userStatus.get());
        return changeToDto(userStatus.get());
    }

    @Override
    public void delete(UUID userId) {
        Optional<UserStatus> userStatus = userStatusRepository.findByUserId(userId);
        if (userStatus.isEmpty()) {
            throw new IllegalArgumentException("UserStatus not found for the given userId.");
        }
        userStatusRepository.deleteByUserId(userId);
    }

    private UserStatusDto changeToDto(UserStatus userStatus) {
        return new UserStatusDto(userStatus.getId(), userStatus.getUserId(), userStatus.getLastLoginTime(), userStatus.getCreatedAt(), userStatus.getUpdatedAt());
    }
}
