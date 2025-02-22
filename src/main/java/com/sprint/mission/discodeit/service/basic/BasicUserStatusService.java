package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateByUserId;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.OnlineStatus;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;

    private final UserRepository userRepository;

    @Override
    public UserStatus create(UserStatusCreateRequest userStatusCreateRequest) {
        if (!userRepository.existsById(userStatusCreateRequest.userId())) {
            throw new NoSuchElementException("[ERROR] 존재하지 않는 유저입니다.");
        }

        findAll().forEach(userStatus -> {
                    if (userStatus.isSameUserId(userStatusCreateRequest.userId())) {
                        throw new IllegalArgumentException("[ERROR] 이미 존재하는 데이터입니다.");
                    }
                });

        return userStatusRepository.save(new UserStatus(userStatusCreateRequest.userId()));
    }

    @Override
    public UserStatus find(UUID userStatusId) {
        return Optional.ofNullable(userStatusRepository.find(userStatusId))
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 상태입니다."));
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        return findAll().stream()
                .filter(userStatus -> userStatus.isSameUserId(userId))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 상태입니다."));
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    @Override
    public OnlineStatus getOnlineStatus(UUID userId) {
        UserStatus userStatus = findByUserId(userId);
        return userStatus.calculateOnlineStatus();
    }

    @Override
    public UserStatus update(UUID userStatusId, UserStatusUpdateRequest userStatusUpdateRequest) {
        UserStatus userStatus = find(userStatusId);
        userStatus.update(userStatusUpdateRequest.lastActiveAt());
        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus updateByUserUd(UUID userId, UserStatusUpdateRequest userStatusUpdateRequest) {
        UserStatus userStatus = findByUserId(userId);
        userStatus.update(userStatusUpdateRequest.lastActiveAt());
        return userStatusRepository.save(userStatus);
    }

    @Override
    public void delete(UUID userStatusId) {
        userStatusRepository.existsById(userStatusId);
        userStatusRepository.delete(userStatusId);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        UserStatus userStatus = findByUserId(userId);
        userStatusRepository.delete(userStatus.getId());
    }
}
