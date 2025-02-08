package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> data;

    public JCFUserRepository() {
        data = new HashMap<>();
    }

    public UUID save(User user) {
        data.put(user.getId(), user);
        return user.getId();
    }

    public User findOne(UUID id) {
        return data.get(id);
    }

    public List<User> findAll() {
/*        if (data.isEmpty()) {
            return Collections.emptyList(); // 빈 리스트 반환
        }*/
        return new ArrayList<>(data.values());
    }

    public UUID update(User user){
        data.put(user.getId(), user);
        return user.getId();
    }

    public UUID delete(UUID id) {
        data.remove(id);
        return id;
    }

}
