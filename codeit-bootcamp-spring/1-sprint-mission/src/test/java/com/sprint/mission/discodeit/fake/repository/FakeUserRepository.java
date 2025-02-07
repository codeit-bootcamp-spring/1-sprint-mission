package com.sprint.mission.discodeit.fake.repository;

import com.sprint.mission.discodeit.domain.user.Email;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.domain.user.Username;
import com.sprint.mission.discodeit.repository.user.interfaces.UserRepository;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class FakeUserRepository implements UserRepository {

    Map<UUID, User> store = new HashMap<>();
    Set<Email> emails = new HashSet<>();
    Set<Username> usernames = new HashSet<>();

    @Override
    public User save(User user) {
        store.put(user.getId(), user);
        emails.add(user.getEmail());
        usernames.add(user.getUsername());
        return user;
    }

    @Override
    public boolean isExistByEmail(Email email) {
        return emails.contains(email);
    }

    @Override
    public boolean isExistByUsername(Username username) {
        return usernames.contains(username);
    }
}
