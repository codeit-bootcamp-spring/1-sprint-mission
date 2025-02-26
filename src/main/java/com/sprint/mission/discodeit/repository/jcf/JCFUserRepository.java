package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFUserRepository implements UserRepository {

    private Map<UUID, User> userMap;

    public JCFUserRepository() {
        this.userMap = new HashMap<>();
    }

    public JCFUserRepository(Map<UUID, User> userMap) {
        this.userMap = userMap;
    }

    @Override
    public User userSave(User user) {
        userMap.put(user.getId(), user);


        return userMap.get(user.getId());
    }

    @Override
    public User findUserById(UUID userId) {
        return userMap.get(userId);
    }

    @Override
    public Map<UUID, User> findAllUser() {
        return userMap;
    }

    @Override
    public void removeUserById(UUID userId) {
        userMap.remove(userId);
    }

    @Override
    public User findUserByEmail(String email) {
        return null;
    }

    @Override
    public void clearData() {
        this.userMap.clear();
    }

    @Override
    public void resetData() {
        this.userMap = new HashMap<>();
    }

    @Override
    public User findUserByName(String userName) {
        return null;
    }
}
