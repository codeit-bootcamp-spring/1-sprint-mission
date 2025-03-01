package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCFUserStatusRepository implements UserStatusRepository {
    Map<UUID, UserStatus> userStatuses = new HashMap<>();

    @Override
    public UserStatus save(UserStatus userStatus) {
        /*
        return userStatuses.put(userStatus.getId(), userStatus);
        HashMap put이 "key" or "null" 을 반환한다. 그래서 여기서 NullPointerException 이 발생해
        진행을 못하고 있었다
         */
        userStatuses.put(userStatus.getUserId(), userStatus);
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findById(UUID userStatusId) {
        return Optional.ofNullable(userStatuses.get(userStatusId));
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return userStatuses.values().stream()
                .filter( userStatus -> userStatus.getUserId().equals(userId) )
                .findFirst();
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatuses.values().stream().toList();
    }

    @Override
    public void deleteById(UUID userStatusId) {
        userStatuses.remove(userStatusId);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        findAll().stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .forEach(userStatus -> deleteByUserId(userStatus.getId()));
    }
}
