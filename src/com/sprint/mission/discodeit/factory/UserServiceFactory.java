package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.service.proxy.UserServiceProxy;

import java.util.function.Supplier;

public enum UserServiceFactory {
    JCF_USER_SERVICE_FACTORY(() -> new UserServiceProxy(new JCFUserService())),
    FILE_USER_SERVICE_FACTORY(() -> new UserServiceProxy(new FileUserService()))
    ;

    private final Supplier<UserService> supplier;

    UserServiceFactory(Supplier<UserService> supplier) {
        this.supplier = supplier;
    }

    public UserService createUserService() {
        return supplier.get();
    }
}
