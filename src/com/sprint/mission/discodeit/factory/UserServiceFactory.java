package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.service.proxy.UserServiceProxy;

import java.util.function.Function;

public enum UserServiceFactory {
    JCF_USER_SERVICE_FACTORY((userRepository) -> new UserServiceProxy(new JCFUserService(userRepository))),
    FILE_USER_SERVICE_FACTORY((userRepository) -> new UserServiceProxy(new FileUserService(userRepository)))
    ;

    private final Function<UserRepository, UserService> function;

    UserServiceFactory(Function<UserRepository, UserService> function) {
        this.function = function;
    }

    public UserService createUserService(UserRepository userRepository) {
        return function.apply(userRepository);
    }
}
