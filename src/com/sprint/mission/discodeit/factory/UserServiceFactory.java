package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.service.UserService;

public interface UserServiceFactory {
    UserService createUserService();
}
