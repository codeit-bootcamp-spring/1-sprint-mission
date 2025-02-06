package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.UserStatusCreateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
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

    @Override
    public UserStatus create(UserStatusCreateRequest request) {
        if (userStatusRepository.findByUserId(request.userId()) != null) {
            throw new IllegalArgumentException("UserStatus already exists for this user.");
        }
        UserStatus userStatus = new UserStatus(request.userId(), Instant.now());
        return userStatusRepository.save(userStatus);
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
    public UserStatus update(UserStatusUpdateRequest request) {
        UserStatus userStatus = userStatusRepository.findByUserId(request.userId());
        if (userStatus == null) {
            throw new IllegalArgumentException("UserStatus not found.");
        }
        userStatus.update(request.lastLoginTime());
        return userStatusRepository.save(userStatus);
    }

    @Override
    public void delete(UUID userId) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId);
        if (userStatus == null) {
            throw new IllegalArgumentException("UserStatus not found for the given userId.");
        }
        userStatusRepository.deleteByUserId(userId);
    }
}
