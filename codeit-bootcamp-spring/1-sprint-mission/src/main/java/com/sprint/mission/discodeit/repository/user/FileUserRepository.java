package com.sprint.mission.discodeit.repository.user;

import com.sprint.mission.discodeit.domain.user.Email;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.domain.user.Username;
import com.sprint.mission.discodeit.repository.user.interfaces.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileUserRepository implements UserRepository {

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public Optional<User> findOneById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findOneByEmail(Email email) {
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public boolean isExistByEmail(Email email) {
        return false;
    }

    @Override
    public boolean isExistByUsername(Username username) {
        return false;
    }

    @Override
    public void deleteByUser(User user) {

    }
}
