package com.sprint.mission.discodeit.repository.jcf.user;

import com.sprint.mission.discodeit.repository.common.InMemoryCrudRepository;
import com.sprint.mission.discodeit.entity.user.entity.User;
import java.util.Optional;
import java.util.UUID;

public class JCFUserRepositoryInMemory extends InMemoryCrudRepository<User, UUID> implements UserRepository {

    private static UserRepository INSTANCE;

    private JCFUserRepositoryInMemory() {}

    @Override
    public Optional<User> findByUsername(String username) {
        var findUser = findAll().stream()
                .filter(user -> username.equals(user.getNicknameValue()))
                .findFirst();

        return findUser;
    }


    public static UserRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new JCFUserRepositoryInMemory();
        }

        return INSTANCE;
    }

    public static UserRepository getUserRepositoryInMemory() {
        return new JCFUserRepositoryInMemory();
    }
}
