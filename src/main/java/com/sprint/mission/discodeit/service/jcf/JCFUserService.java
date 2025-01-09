package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.BaseService;

import java.util.*;

public class JCFUserService implements BaseService<User> {
    private final Map<UUID, User> data = new HashMap<>();

    @Override
    public User create(User user) {
        if (isUsernameDuplicate(user.getUsername())) {
            throw new IllegalArgumentException("유저 이름이 이미 존재합니다.");
        }

        if (!isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("유효하지 않은 이메일 형식입니다.");
        }

        data.put(user.getId(), user);
        return user;
    }

    @Override
    public User readById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public User update(UUID id, User user) {
        User checkUser = data.get(id);

        if (isUsernameDuplicate(user.getUsername())) {
            throw new IllegalArgumentException("유저 이름이 이미 존재합니다.");
        }

        if (!isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("유효하지 않은 이메일 형식입니다.");
        }

        checkUser.update(user.getNickname(), user.getUsername(), user.getEmail(), user.getPhoneNumber());
        return checkUser;
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }

    private boolean isUsernameDuplicate(String username) {
        return data.values().stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
}
