package com.sprint.mission.repository.jcf.addOn;

import com.sprint.mission.entity.addOn.UserStatus;
import com.sprint.mission.entity.main.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserStatusRepository {

    private final Map<UUID, UserStatus> statusMap = new HashMap<>();

    public void save(UserStatus userStatus) {
        statusMap.put(userStatus.getUserId(), userStatus);
    }

    public Optional<UserStatus> findById(UUID userId) {
        return Optional.of(statusMap.get(userId));
    }

    public List<UserStatus> findAll() {
        return new ArrayList<>(statusMap.values());
    }

    public Boolean isExistById(UUID userId) {
        return statusMap.get(userId) != null;
    }

    public void delete(UUID userId) {
        statusMap.remove(userId);
    }

    public Map<User, UserStatus> findStatusMapByUser(List<User> userList) {
        Map<User, UserStatus> userStatusMap = new HashMap<>();
        userList.forEach(user ->
                userStatusMap.put(user, statusMap.getOrDefault(user.getId(), null)));
        return userStatusMap;
    }
}
