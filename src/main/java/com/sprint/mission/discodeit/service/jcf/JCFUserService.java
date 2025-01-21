package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {

    private static final JCFUserService jcfUserService = new JCFUserService();
    private final Map<UUID, User> data;

    private JCFUserService() {
        this.data = new HashMap<>();
    }

    public static JCFUserService getInstance() {
        return jcfUserService;
    }

    @Override
    public User createUser(UserDto userDto) {
        User user = new User(userDto.getName(), userDto.getLoginId(), userDto.getPassword());
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public User readUser(UUID userId) {
        User user = data.get(userId);
        if (user == null) {
            throw new RuntimeException("등록되지 않은 user입니다.");
        }
        return user;
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateUser(UUID userId, UserDto userDto) {
        User user = data.get(userId);
        if (user == null) {
            throw new RuntimeException("등록되지 않은 user입니다.");
        }
        user.updateLoginId(userDto.getLoginId());
        user.updatePassword(userDto.getPassword());
        user.updateName(userDto.getName());
    }

    @Override
    public void deleteUser(UUID userId) {
        data.remove(userId);
    }
}
