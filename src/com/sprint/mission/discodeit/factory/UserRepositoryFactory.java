package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.factory.type.repository.UserRepositoryType;
import com.sprint.mission.discodeit.repository.UserRepository;

public class UserRepositoryFactory {
    public static UserRepository createRepository(UserRepositoryType repositoryType) {
        return repositoryType.create();
    }
}
