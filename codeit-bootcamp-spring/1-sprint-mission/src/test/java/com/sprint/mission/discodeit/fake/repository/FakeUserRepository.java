package com.sprint.mission.discodeit.fake.repository;

import com.sprint.mission.discodeit.domain.user.Email;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.domain.user.Username;
import com.sprint.mission.discodeit.repository.user.interfaces.UserRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FakeUserRepository implements UserRepository {

    Map<UUID, User> uuidUsers = new HashMap<>();
    Map<Email, User> emailUsers = new HashMap<>();
    Map<Username, User> usernameUsers = new HashMap<>();

    @Override
    public User save(User user) {
        uuidUsers.put(user.getId(), user);
        emailUsers.put(user.getEmail(), user);
        usernameUsers.put(user.getUsername(), user);
        return user;
    }

    @Override
    public Optional<User> findOneByEmail(Email email) {
        User foundUser = emailUsers.get(email);
        return Optional.ofNullable(foundUser);
    }

    @Override
    public boolean isExistByEmail(Email email) {
        return emailUsers.containsKey(email);
    }

    @Override
    public boolean isExistByUsername(Username username) {
        return usernameUsers.containsKey(username);
    }
}
