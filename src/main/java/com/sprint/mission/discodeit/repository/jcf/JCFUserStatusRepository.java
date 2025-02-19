package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jsf")
public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> data;

    public JCFUserStatusRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        this.data.put(userStatus.getUser().getId(), userStatus);
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findByUser(User user) {
        return Optional.ofNullable(this.data.get(user.getId()));
    }

    @Override
    public void deleteByUser(User user) {
        this.data.remove(user.getId());
    }

    @Override
    public boolean existsByUser(User user) {
        return this.data.containsKey(user.getId());
    }

    @Override
    public List<UserStatus> findAllByUserIn(List<User> users) {
        return users.stream()
                .map(user -> this.data.get(user.getId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
