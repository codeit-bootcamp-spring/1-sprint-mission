package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {

    private static final JCFUserRepository jcfUserRepository = new JCFUserRepository();
    private final Map<UUID, User> data;

    private JCFUserRepository() {
        data = new HashMap<>(100);
    }

    public static JCFUserRepository getInstance() {
        return jcfUserRepository;
    }

    @Override
    public User save(User user) {
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public User findUser(UUID userId) {
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
