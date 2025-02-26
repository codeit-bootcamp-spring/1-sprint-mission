package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
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
    public UserStatus create(UserStatusCreateRequest request) {
        User user = userRepository.findById(request.userId());
        if (user == null) {
            throw new IllegalArgumentException("User not found with id: " + request.userId());
        }

        UserStatus existingStatus = userStatusRepository.findByUserId(request.userId());
        if (existingStatus != null) {
            throw new IllegalStateException("UserStatus already exists for user: " + request.userId());
        }

        Instant lastActivityAt = request.lastActivityAt() != null ?
                request.lastActivityAt() :
                Instant.now();

        UserStatus userStatus = new UserStatus(request.userId(), lastActivityAt);
        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus findById(UUID id) {
        return userStatusRepository.findById(id);
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        return userStatusRepository.findByUserId(userId);
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    @Override
    public UserStatus update(UUID id, UserStatusUpdateRequest request) {
        UserStatus userStatus = userStatusRepository.findById(id);
        if (userStatus == null) {
            throw new IllegalArgumentException("UserStatus not found with id: " + id);
        }

        userStatus.updateLastActivityAt(request.updateLastActivityAt());
        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus updateByUserId(UUID userId, UserStatusUpdateRequest request) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId);
        if (userStatus == null) {
            throw new IllegalArgumentException("UserStatus not found for user: " + userId);
        }

        userStatus.updateLastActivityAt(request.updateLastActivityAt());
        return userStatusRepository.save(userStatus);
    }

    @Override
    public void deleteById(UUID id) {
        userStatusRepository.deleteById(id);
    }
}