package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.conflict.DuplicateUsernameException;
import com.sprint.mission.discodeit.exception.validation.InvalidEmailException;
import com.sprint.mission.discodeit.exception.validation.InvalidUsernameException;
import com.sprint.mission.discodeit.service.BaseService;

import java.util.*;

public class JCFUserService implements BaseService<User> {
    private final Map<UUID, User> data = new HashMap<>();

    @Override
    public User create(User user) {
        validUser(user);

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

        validUser(user);

        checkUser.update(user.getNickname(), user.getUsername(), user.getEmail(), user.getPhoneNumber());
        return checkUser;
    }

    private void validUser(User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new InvalidUsernameException("유효하지 않은 이름명입니다.");
        }

        if (isUsernameDuplicate(user.getUsername())) {
            throw new DuplicateUsernameException("유저 이름이 이미 존재합니다.");
        }

        if (!isValidEmail(user.getEmail())) {
            throw new InvalidEmailException("유효하지 않은 이메일 형식입니다.");
        }
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }

    private boolean isUsernameDuplicate(String username) {
        return data.values().stream().anyMatch(user -> user.getUsername().equals(username));
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
}
