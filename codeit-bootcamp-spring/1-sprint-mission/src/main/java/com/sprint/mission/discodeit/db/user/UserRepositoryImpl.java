package com.sprint.mission.discodeit.db.user;

import com.sprint.mission.discodeit.db.common.InMemoryCrudRepository;
import com.sprint.mission.discodeit.db.user.ifs.UserRepository;
import com.sprint.mission.discodeit.entity.user.User;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryImpl extends InMemoryCrudRepository<User, UUID> implements UserRepository {

    @Override
    public Optional<User> findByUsername(String username) {
        var findUser = findAll().stream()
                .filter(user -> username.equals(user.getName()))
                .findFirst();
        return findUser;
    }
}
