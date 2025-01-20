package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.proxy.UserRepositoryProxy;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.service.proxy.UserServiceProxy;

import java.util.function.Function;
import java.util.function.Supplier;

public enum UserRepositoryFactory {
    JCF_USER_REPOSITORY_FACTORY(() -> new UserRepositoryProxy(new JCFUserRepository())),
    FILE_USER_REPOSITORY_FACTORY(() -> new UserRepositoryProxy(new FileUserRepository()))
    ;

    private final Supplier<UserRepository> supplier;

    UserRepositoryFactory(Supplier<UserRepository> supplier) {
        this.supplier = supplier;
    }

    public UserRepository createUserRepository() {
        return supplier.get();
    }
}
