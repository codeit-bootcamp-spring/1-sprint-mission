package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.validator.UserValidator;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {

    private final UserValidator validator;
    private final Map<UUID, User> users;

    private JCFUserService() {
        this.validator = new UserValidator();
        this.users = new HashMap<>();
    }

    private static class JCFUserServiceHolder {
        private static final UserService INSTANCE = new JCFUserService();
    }

    public static UserService getInstance() {
        return JCFUserServiceHolder.INSTANCE;
    }

    @Override
    public User create(String name, String email, String phoneNumber, String password) {
        validator.validate(name, email, phoneNumber);
        isDuplicateEmail(email);
        isDuplicatePhoneNumber(phoneNumber);
        User user = new User(name, email, phoneNumber, password);
        users.put(user.getId() ,user);
        return user;
    }

    @Override
    public User find(UUID userId) {
        User foundUser = users.get(userId);

        return Optional.ofNullable(foundUser)
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 유저입니다."));
    }

    @Override
    public List<User> findAll() {
        return users.values().stream().toList();
    }

    @Override
    public String getInfo(UUID userId) {
        User user = find(userId);
        return user.toString();
    }

    @Override
    public void update(UUID userId, String name, String email, String phoneNumber) {
        User user = find(userId);
        validator.validate(name, email, phoneNumber);

        user.update(name, email, phoneNumber);
    }

    @Override
    public void updatePassword(UUID userId, String originalPassword, String newPassword) {
        User user = find(userId);
        user.updatePassword(originalPassword, newPassword);
        user.updateUpdatedAt();
    }

    @Override
    public void delete(UUID userId) {
        if (!users.containsKey(userId)) {
            throw new NoSuchElementException("[ERROR] 존재하지 않는 유저입니다.");
        }
        users.remove(userId);
    }

    @Override
    public void isDuplicateEmail(String email) {
        users.values().forEach(user -> user.isDuplicateEmail(email));
    }

    @Override
    public void isDuplicatePhoneNumber(String phoneNumber) {
        users.values().forEach(user -> user.isDuplicatePhoneNumber(phoneNumber));
    }
}
