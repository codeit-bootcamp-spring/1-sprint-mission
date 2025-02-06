package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserStatusDto create(UserStatusCreateRequest request) {
        if (userStatusRepository.findByUserId(request.userId()).isPresent()) {
            throw new IllegalArgumentException("UserStatus already exists for this user.");
        }
        UserStatus userStatus = new UserStatus(request.userId(), Instant.now());
        userStatusRepository.save(userStatus);
        return changeToDto(userStatus);
    }

    @Override
    public UserStatusDto findByUserId(UUID userId) {
        Optional<UserStatus> userStatus = userStatusRepository.findByUserId(userId);
        return changeToDto(Objects.requireNonNull(userStatus.orElse(null)));
    }

    @Override
    public List<UserStatusDto> findAll() {
        List<UserStatus> userStatus = userStatusRepository.findAll();
        return userStatus.stream()
                .map(status -> changeToDto((UserStatus) status))
                .collect(Collectors.toList());
    }

    @Override
    public UserStatusDto update(UserStatusUpdateRequest request) {
        Optional<UserStatus> userStatus = userStatusRepository.findByUserId(request.userId());
        if (userStatus.isEmpty()) {
            throw new IllegalArgumentException("UserStatus not found.");
        }
        userStatus.get().update(request.lastLoginTime());
        userStatusRepository.save(userStatus.orElse(null));
        return changeToDto(userStatus.orElse(null));
    }

    @Override
    public void delete(UUID userId) {
        Optional<UserStatus> userStatus = userStatusRepository.findByUserId(userId);
        if (userStatus.isEmpty()) {
            throw new IllegalArgumentException("UserStatus not found for the given userId.");
        }
        userStatusRepository.deleteByUserId(userId);
    }

    private UserStatusDto changeToDto(UserStatus userStatus){
        return new UserStatusDto(
                userStatus.getId(),
                userStatus.getUserId(),
                userStatus.getLastLoginTime(),
                userStatus.getCreatedAt(),
                userStatus.getUpdatedAt()
        );
    }
}
