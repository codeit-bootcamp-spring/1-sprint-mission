package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> data ;
    public JCFUserRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public void save(User user) {
        data.put(user.getId(), user);
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return Optional.ofNullable(this.data.get(userId));
    }

    @Override
    public Map<UUID, User> findAll() {
        return new HashMap<>(data);
    }

    @Override
    public void delete(UUID userId) {
        data.remove(userId);
    }

    @Override
    public boolean existsById(UUID userId) {
        return data.containsKey(userId);
    }
}
