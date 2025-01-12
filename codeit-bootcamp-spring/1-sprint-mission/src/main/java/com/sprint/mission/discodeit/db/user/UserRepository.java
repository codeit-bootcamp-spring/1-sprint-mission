package com.sprint.mission.discodeit.db.user;

import com.sprint.mission.discodeit.db.common.CrudRepository;
import com.sprint.mission.discodeit.entity.user.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {

    Optional<User> findByUsername(String username);



    public static UserRepository getInMemoryUserRepositoryImpl() {
        return UserRepositoryImpl.getInstance();
    }
}
