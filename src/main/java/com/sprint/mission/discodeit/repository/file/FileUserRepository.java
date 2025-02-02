package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

public class FileUserRepository extends AbstractFileRepository<User> implements UserRepository {

    public FileUserRepository(String FILE_PATH) {
        super(FILE_PATH);
    }

    @Override
    public Optional<User> findByName(String name) {
        return data.values().stream()
                .filter(user -> user.getName().equals(name))
                .findFirst();
    }

    @Override
    public boolean update(UUID id, String name, String email, UserStatus status) {
        boolean updated = data.computeIfPresent(id, (key, u) -> {
            u.update(name, email, status);
            return u;
        }) != null;
        if (updated) {
            saveData();
        }
        return updated;
    }
}