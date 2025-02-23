package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.entity.UserStatus;
import com.sprint.mission.discodeit.web.dto.UserStatusUpdateDto;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatus create(UserStatus userStatus) {
        UUID userId = userStatus.getUserId();
        if (userId == null) {
            throw new IllegalArgumentException("User id가 존재하지 않습니다.");
        }
        if(userStatusRepository.findByUserId(userId).equals(userStatus)) {
            throw new IllegalArgumentException("이미 해당 유저가 존재합니다.");
        }
        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus find(UUID userStatusId) {
        return userStatusRepository.findById(userStatusId).get();
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll();
    }

    @Override
    public UserStatus update(UUID userStatusId, UserStatusUpdateDto updateUserStatus) {
        Instant newLastAttendAt = updateUserStatus.getNewAttendAt();
        UserStatus userStatus = userStatusRepository.findById(userStatusId).get();
        userStatus.updateUserStatus(newLastAttendAt);
        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus updateByUserId(UUID userId, UserStatusUpdateDto updateUserStatus) {
        Instant newLastAttendAt = updateUserStatus.getNewAttendAt();
        UserStatus userStatus = userStatusRepository.findByUserId(userId).get();
        userStatus.updateUserStatus(newLastAttendAt);
        return userStatusRepository.save(userStatus);
    }

    @Override
    public void delete(UUID userStatusId) {
        userStatusRepository.deleteById(userStatusId);
    }

    public boolean isUserOnline(UUID userId) {
        if (userStatusRepository.findByUserId(userId).isEmpty()) {
            return false;
        } else {
            UserStatus userStatus = userStatusRepository.findByUserId(userId).get();
            if(userStatus.isOnline()) {
                return true;
            }
            return false;
        }
    }

    public List<UUID> getOnlineUsers() {
        List<UserStatus> all = userStatusRepository.findAll();
        List<UUID> userIds = new ArrayList<>();
        for (UserStatus userStatus : all) {
            if(!userStatus.isOnline()) {
                log.info("오프라인");
            }
            userIds.add(userStatus.getUserId());
        }
        return userIds;
    }
}
