package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.notfound.NotfoundIdException;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

import static com.sprint.mission.discodeit.util.UserUtil.validEmail;
import static com.sprint.mission.discodeit.util.UserUtil.validUsername;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data;

    public JCFUserService() {
        data = new HashMap<>();
    }

    @Override
    public User create(String username, String email, String phoneNumber, String password) {
        validUsername(username);
        validEmail(email);

        User user = new User(username, email, phoneNumber, password);
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public User findById(UUID userId) {
        return data.get(userId);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public User update(UUID userId, String username, String email, String phoneNumber, String password) {
        validUsername(username);
        validEmail(email);

        User checkUser = data.get(userId);
        checkUser.update(username, email, phoneNumber, password);
        return checkUser;
    }

    @Override
    public void delete(UUID userId) {
        if (!data.containsKey(userId)) {
            throw new NotfoundIdException("유효하지 않은 Id입니다.");
        }
        data.remove(userId);
    }
}
