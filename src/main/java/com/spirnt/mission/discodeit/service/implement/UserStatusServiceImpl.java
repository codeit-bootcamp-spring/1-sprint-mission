package com.spirnt.mission.discodeit.service.implement;

import com.spirnt.mission.discodeit.dto.userStatus.UserStatusCreate;
import com.spirnt.mission.discodeit.dto.userStatus.UserStatusUpdate;
import com.spirnt.mission.discodeit.enity.UserStatus;
import com.spirnt.mission.discodeit.enity.UserStatusType;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.repository.UserStatusRepository;
import com.spirnt.mission.discodeit.service.UserService;
import com.spirnt.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserStatusServiceImpl implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatus create(UserStatusCreate userStatusCreate) {
        // User가 존재하지 않으면 예외 발생
        if (!userRepository.existsById(userStatusCreate.userId()))
            throw new NoSuchElementException("User ID Not Found");
        // 이미 User와 관련된 객체가 존재하면 예외 발생
        if (userStatusRepository.findAll().values().stream()
                .anyMatch(userStatus -> userStatus.getUserId().equals(userStatusCreate.userId()))) {
            throw new IllegalStateException("The ReadStatus with UserId and ChannelId Already Exists");
        }
        UserStatus userStatus = new UserStatus(userStatusCreate.userId(),
                userStatusCreate.type(), Instant.now());
        userStatusRepository.save(userStatus);
        return userStatus;
    }

    @Override
    public UserStatus find(UUID userStatusId) {
        UserStatus userStatus = userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> new NoSuchElementException("UserStatus ID Not Found"));
        // 만약 마지막으로 기록된 접속 시간으로부터 5분 이상 지나있으면 OFFLINE으로 업데이트 후 반환
        if(userStatus.getUserStatusType()==UserStatusType.ONLINE && !userStatus.isOnline()) {
            update(userStatusId, new UserStatusUpdate(UserStatusType.OFFLINE,userStatus.getLastSeenAt()));  // 마지막 접속 시간은 그대로 유지
        }
        return userStatus;
    }

    @Override
    public List<UserStatus> findAll() {
        Map<UUID, UserStatus> map = userStatusRepository.findAll();
        List<UserStatus> list = map.values().stream()
                .map(userStatus -> this.find(userStatus.getId()))
                .collect(Collectors.toList());
        return list;
    }

    @Override
    public UserStatus update(UUID userStatusId, UserStatusUpdate userStatusUpdate) {
        UserStatus userStatus = userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> new NoSuchElementException("UserStatus ID Not Found"));
        userStatus.update(userStatusUpdate.lastSeenAt());
        userStatusRepository.save(userStatus);
        return userStatus;
    }

    @Override
    public UserStatus updateByUserId(UUID userId, UserStatusUpdate userStatusUpdate) {
        Map<UUID, UserStatus> map = userStatusRepository.findAll();
        UserStatus userStatus = map.values().stream()
                .filter(value -> value.getUserId().equals(userId))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("UserStatus ID Not Found"));
        userStatus = this.update(userStatus.getId(), userStatusUpdate);
        return userStatus;
    }

    @Override
    public void delete(UUID userStatusId) {
        userStatusRepository.delete(userStatusId);
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        List<UserStatus> list = findAll();
        UUID userStatusId = list.stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .map(userStatus -> userStatus.getId())
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("UserStatus ID Not Found"));
        return find(userStatusId);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        Map<UUID, UserStatus> map = userStatusRepository.findAll();
        UserStatus userStatus = map.values().stream()
                .filter(value -> value.getUserId().equals(userId))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("UserStatus ID Not Found"));
        delete(userStatus.getId());
    }
}
