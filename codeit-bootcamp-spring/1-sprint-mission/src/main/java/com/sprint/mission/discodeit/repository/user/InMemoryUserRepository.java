package com.sprint.mission.discodeit.repository.user;

import com.sprint.mission.discodeit.domain.user.Email;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.repository.user.interfaces.UserRepository;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryUserRepository implements UserRepository {

    Map<UUID, User> store = new HashMap<>();
    Set<Email> emails = new HashSet<>();

    @Override
    public User save(User user) {
        store.put(user.getId(),user);
        emails.add(user.getEmail());
        return user;
    }

    @Override
    public boolean isExistByEmail(Email email) {
        return emails.contains(email);
    }
}
