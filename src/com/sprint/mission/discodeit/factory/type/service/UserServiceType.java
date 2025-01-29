package com.sprint.mission.discodeit.factory.type.service;

import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.service.proxy.UserServiceProxy;

import java.util.function.Function;

public enum UserServiceType {
    JCF(JCFUserService::new),
    FILE(FileUserService::new)
    ;

    private final Function<UserRepository, UserService> function;

    UserServiceType(Function<UserRepository, UserService> function) {
        this.function = function;
    }

    public UserService create(UserRepository userRepository) {
        return new UserServiceProxy(function.apply(userRepository));
    }
}
