package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateByUserIdDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateDto;
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
    public UserStatus create(UserStatusCreateDto userStatusCreateDto) {
        if (!userRepository.existsById(userStatusCreateDto.userId())) {
            throw new NoSuchElementException("[ERROR] 존재하지 않는 유저입니다.");
        }

        findAll().forEach(userStatus -> {
                    if (userStatus.isSameUserId(userStatusCreateDto.userId())) {
                        throw new IllegalArgumentException("[ERROR] 이미 존재하는 데이터입니다.");
                    }
                });

        return userStatusRepository.save(new UserStatus(userStatusCreateDto.userId()));
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
    public UserStatus update(UserStatusUpdateDto userStatusUpdateDto) {
        UserStatus userStatus = find(userStatusUpdateDto.id());
        userStatus.update();
        return userStatus;
    }

    @Override
    public UserStatus updateByUserUd(UserStatusUpdateByUserIdDto userStatusUpdateByUserIdDto) {
        UserStatus userStatus = findByUserId(userStatusUpdateByUserIdDto.userId());
        userStatus.update();
        return userStatus;
    }

    @Override
    public void deleteByUserId(UUID userId) {
        UserStatus userStatus = findByUserId(userId);
    }
}
