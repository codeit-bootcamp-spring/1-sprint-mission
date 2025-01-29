package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.factory.type.repository.UserRepositoryType;
import com.sprint.mission.discodeit.factory.type.service.UserServiceType;
import com.sprint.mission.discodeit.service.UserService;

public class UserServiceFactory {
    public static UserService createService(UserServiceType serviceType, UserRepositoryType repositoryType) {
        return serviceType.create(repositoryType.create());
    }
}
