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
        // validateUserStatus.validateUserStatus(request.userId());

        UUID userId = request.userId();
        Instant lastActiveAt = request.lastActiveAt();
        UserStatus userStatus = new UserStatus(userId, lastActiveAt);
        userStatusRepository.save(userStatus);
        return changeToDto(userStatus);
    }

    @Override
    public UserStatusDto findById(UUID userStatusId) {
        UserStatus userStatus = userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> new ResourceNotFoundException("User status not found."));
        return changeToDto(userStatus);
    }

    @Override
    public List<UserStatusDto> findAll() {
        return userStatusRepository.findAll().stream()
                .map(this::changeToDto)
                .toList();
    }

    @Override
    public UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest request) {
        UserStatus userStatus = userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> new ResourceNotFoundException("User status not found."));
        Instant newLastActiveAt = request.newLastActiveAt();
        userStatus.update(newLastActiveAt);
        UserStatus updatedUserStatus = userStatusRepository.save(userStatus);
        return changeToDto(updatedUserStatus);
    }

    @Override
    public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User status not found."));
        Instant newLastActiveAt = request.newLastActiveAt();
        userStatus.update(newLastActiveAt);
        UserStatus updatedUserStatus = userStatusRepository.save(userStatus);
        return changeToDto(updatedUserStatus);
    }

    @Override
    public void delete(UUID userStatusId) {
        if (!userStatusRepository.existsById(userStatusId)) {
            throw new ResourceNotFoundException("User Status not found.");
        }
        userStatusRepository.deleteById(userStatusId);
    }

    private UserStatusDto changeToDto(UserStatus userStatus) {
        return new UserStatusDto(
                userStatus.getId(),
                userStatus.getUserId(),
                userStatus.getLastActiveAt(),
                userStatus.getCreatedAt(),
                userStatus.getUpdatedAt()
        );
    }
}
