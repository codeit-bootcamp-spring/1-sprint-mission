package com.sprint.mission.discodeit.repository.jcf.user;

import com.sprint.mission.discodeit.repository.common.CrudRepository;
import com.sprint.mission.discodeit.entity.user.entity.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    public static UserRepository getInMemoryUserRepositoryImpl() {
        return JCFUserRepositoryInMemory.getInstance();
    }

}
