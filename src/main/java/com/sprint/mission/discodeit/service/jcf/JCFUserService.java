package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    //데이터를 저장할 수 있는 필드 -> Map 사용
    private final Map<UUID, User> data = new HashMap<>();
    @Override
    public User createUser(String username) {
       User user = new User(username);
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserId(UUID id) {
        return data.get(id);
    }


    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateUser(UUID id, String username) {
        User user = data.get(id);
        if (user != null) {
            user.updateUsername(username);
        }
    }

    @Override
    public void deleteUser(UUID id) {
        data.remove(id);
    }
}
