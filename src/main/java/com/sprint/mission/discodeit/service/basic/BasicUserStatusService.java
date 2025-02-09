package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.validation.ValidateUserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final ValidateUserStatus validateUserStatus;

    @Override
    public UserStatusDto create(UserStatusCreateRequest request) {
        validateUserStatus.validateUserStatus(request.userId());

        UserStatus userStatus = new UserStatus(request.userId(), Instant.now());
        userStatusRepository.save(userStatus);
        return changeToDto(userStatus);
    }

    @Override
    public UserStatusDto findByUserId(UUID userId) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("UserStatus not found."));
        return changeToDto(userStatus);
    }

    @Override
    public List<UserStatusDto> findAll() {
        List<UserStatus> userStatuses = userStatusRepository.findAll();
        return userStatuses.stream()
                .map(this::changeToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserStatusDto update(UserStatusUpdateRequest request) {
        UserStatus userStatus = userStatusRepository.findByUserId(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("UserStatus not found."));
        userStatus.update(request.lastLoginTime());
        userStatusRepository.save(userStatus);
        return changeToDto(userStatus);
    }

    @Override
    public void delete(UUID userId) {
        userStatusRepository.deleteByUserId(userId);
    }

    private UserStatusDto changeToDto(UserStatus userStatus) {
        return new UserStatusDto(userStatus.getId(), userStatus.getUserId(), userStatus.getLastLoginTime(), userStatus.getCreatedAt(), userStatus.getUpdatedAt());
    }
}
