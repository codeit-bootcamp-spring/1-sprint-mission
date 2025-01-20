package com.sprint.mission.discodeit.db.user;

import com.sprint.mission.discodeit.db.common.InMemoryCrudRepository;
import com.sprint.mission.discodeit.entity.user.entity.User;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryInMemory extends InMemoryCrudRepository<User, UUID> implements UserRepository {

    private static UserRepository INSTANCE;

    private UserRepositoryInMemory() {}

    @Override
    public Optional<User> findByUsername(String username) {
        var findUser = findAll().stream()
                .filter(user -> username.equals(user.getName()))
                .findFirst();

        return findUser;
    }


    public static UserRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserRepositoryInMemory();
        }

        return INSTANCE;
    }

    public static UserRepository getUserRepositoryInMemory() {
        return new UserRepositoryInMemory();
    }
}
