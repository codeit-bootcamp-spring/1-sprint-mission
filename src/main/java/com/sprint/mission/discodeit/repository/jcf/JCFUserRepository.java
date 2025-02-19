package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> data;

    public JCFUserRepository() {
        data = new HashMap<>(100);
    }

    @Override
    public User save(User user) {
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public User findById(UUID userId) {
        return Optional.ofNullable(data.get(userId))
                .orElseThrow(() -> new NotFoundException("등록되지 않은 user입니다."));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateUser(User user) {
        data.put(user.getId(), user);
    }

    @Override
    public void deleteUser(UUID userId) {
        data.remove(userId);
    }
}
