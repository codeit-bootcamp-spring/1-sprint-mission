package com.sprint.mission.discodeit.service.featureService;

import com.sprint.mission.discodeit.dto.user.UserStatusCreate;
import com.sprint.mission.discodeit.dto.user.UserStatusUpdate;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserStatusServiceImpl implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    @Override
    public UserStatus create(UserStatusCreate userStatusCreate) {
        if(userStatusRepository.findAll().values().stream()
                .anyMatch(userStatus ->  userStatus.getUserId().equals(userStatusCreate.userId()))) {
            throw new IllegalStateException("UserId와 ChannelId가 이미 존재합니다.");
        }
        UserStatus userStatus = new UserStatus(userStatusCreate);
        userStatusRepository.save(userStatus);
        return userStatus;
    }

    @Override
    public UserStatus findById(UUID userStatusId) {
        return userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> new NoSuchElementException("찾을 수 없습니다."));
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        List<UserStatus> userStatusList = findAll();
        UUID userStatusId = userStatusList.stream().filter(userStatus ->
                        userStatus.getUserId().equals(userId)).map(userStatus -> userStatus.getId())
                .findAny().orElseThrow(() -> new NoSuchElementException("찾을 수 없습니다."));
        return findById(userStatusId);
    }

    @Override
    public List<UserStatus> findAll() {
        Map<UUID, UserStatus> userStatusMap = userStatusRepository.findAll();
        List<UserStatus> userStatusList = userStatusMap.values().stream().collect(Collectors.toList());
        return userStatusList;
    }

    @Override
    public UserStatus update(UUID userStatusId, UserStatusUpdate userStatusUpdate) {
        UserStatus userStatus = userStatusRepository.findById(userStatusId)
                .orElseThrow(()-> new NoSuchElementException("찾을 수 없습니다."));
        userStatus.update(userStatusUpdate);
        userStatusRepository.save(userStatus);
        return userStatus;
    }

    @Override
    public UserStatus updateByUserId(UUID userId, UserStatusUpdate userStatusUpdate) {
        Map<UUID, UserStatus> userStatusMap = userStatusRepository.findAll();
        UserStatus userStatus = userStatusMap.values().stream().filter(value ->
                value.getUserId().equals(userId)).findAny().orElseThrow(() ->
                new NoSuchElementException("찾을 수 없습니다."));
        userStatus.update(userStatusUpdate);
        userStatusRepository.save(userStatus);
        return userStatus;
    }

    @Override
    public void delete(UUID userStatusId) {
        userStatusRepository.delete(userStatusId);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        Map<UUID,UserStatus> userStatusMap = userStatusRepository.findAll();
        UserStatus userStatus = userStatusMap.values().stream().filter(value ->
                value.getUserId().equals(userId)).findAny().orElseThrow(()-> new NoSuchElementException("찾을 수 없습니다."));
        delete(userStatus.getId());

    }
}
