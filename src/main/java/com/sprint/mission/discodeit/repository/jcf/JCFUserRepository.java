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
    private final HashMap<String, User> usernameData;
    private final HashMap<String, User> userEmailData;

    public JCFUserRepository() {
        this.usernameData = new HashMap<>();
        this.data = new HashMap<>();
        this.userEmailData = new HashMap<>();
    }

    @Override
    public User save(User user) {
        data.put(user.getId(), user);
        usernameData.put(user.getUsername(), user);
        userEmailData.put(user.getEmail(), user);
        return user;
    }

    @Override
    public boolean delete(String userId) {
        User user = data.get(userId);
        usernameData.remove(user.getUsername());
        userEmailData.remove(user.getEmail());
        return data.remove(userId) != null;
    }

    @Override
    public User findById(String id) {
        return data.get(id);
    }

    @Override
    public User findByUsername(String username) {
        return usernameData.get(username);
    }

    @Override
    public User findByEmail(String email) {
        return userEmailData.get(email);
    }

    @Override
    public List<User> findAll() {
        return data.values().stream().toList();
    }
}
