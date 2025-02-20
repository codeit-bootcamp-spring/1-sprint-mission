package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
@ConditionalOnProperty(name="discodeit.repository.type", havingValue = "jcf")
public class JCFUserRepository implements UserRepository {

    private final HashMap<String, User> data;

    public JCFUserRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public User save(User user) {
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public boolean delete(User user) {
        return data.remove(user.getId()) != null;
    }

    @Override
    public User findById(String id) {
        return data.get(id);
    }

    @Override
    public List<User> findAll() {
        return data.values().stream().toList();
    }
}
