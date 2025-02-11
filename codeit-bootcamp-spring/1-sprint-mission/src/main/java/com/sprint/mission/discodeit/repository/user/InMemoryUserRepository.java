package com.sprint.mission.discodeit.repository.user;

import com.sprint.mission.discodeit.domain.user.Email;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.domain.user.Username;
import com.sprint.mission.discodeit.repository.user.interfaces.UserRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryUserRepository implements UserRepository {

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
    public Optional<User> findOneById(UUID id) {
        return Optional.ofNullable(uuidUsers.get(id));
    }

    @Override
    public Optional<User> findOneByEmail(Email email) {
        return Optional.ofNullable(emailUsers.get(email));
    }

    @Override
    public List<User> findAll() {
        return uuidUsers.values().stream().toList();
    }

    @Override
    public boolean isExistByEmail(Email email) {
        return emailUsers.containsKey(email);
    }

    @Override
    public boolean isExistByUsername(Username username) {
        return usernameUsers.containsKey(username);
    }

    @Override
    public void deleteByUser(User user) {
        uuidUsers.remove(user.getId());
        emailUsers.remove(user.getEmail());
        usernameUsers.remove(user.getUsername());
    }
}
