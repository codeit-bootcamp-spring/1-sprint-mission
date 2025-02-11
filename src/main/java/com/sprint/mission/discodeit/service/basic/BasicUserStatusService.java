package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BasicUserStatusService implements UserStatusService {
    private static final Logger log = LoggerFactory.getLogger(BasicUserStatusService.class);
    private final UserStatusRepository userStatusRepository;

    public BasicUserStatusService(UserStatusRepository userStatusRepository) {
        this.userStatusRepository = userStatusRepository;
    }

    @Override
    public UserStatusDto createUserStatus(UUID userId, Instant lastActive) {
        UserStatus userStatus = new UserStatus(userId, lastActive);
        userStatusRepository.save(userStatus);
        return new UserStatusDto(
                userStatus.getId(),
                userStatus.getUserId(),
                userStatus.getLastActive()
        );
    }

    @Override
    public UserStatusDto findUserStatus(UUID userId) {
        UserStatus userStatus = userStatusRepository.findAll().stream()
                .filter(us -> us.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("해당 사용자로 UserStatus를 찾을 수 없습니다: " + userId));
        return new UserStatusDto(
                userStatus.getId(),
                userStatus.getUserId(),
                userStatus.getLastActive()
        );
    }

    @Override
    public List<UserStatusDto> findAllUserStatus() {
        return userStatusRepository.findAll().stream()
                .map(us -> new UserStatusDto(
                        us.getId(),
                        us.getUserId(),
                        us.getLastActive()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public UserStatusDto updateUserStatus(UUID userId) {
        UserStatus userStatus = userStatusRepository.findAll().stream()
                .filter(us -> us.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("해당 사용자로 UserStatus를 찾을 수 없습니다: " + userId));

        userStatus.updateLastActive(Instant.now());
        userStatusRepository.save(userStatus);

        return new UserStatusDto(
                userStatus.getId(),
                userStatus.getUserId(),
                userStatus.getLastActive()
        );
    }

    @Override
    public void deleteUserStatus(UUID id) {
        userStatusRepository.deleteById(id);
    }
}
