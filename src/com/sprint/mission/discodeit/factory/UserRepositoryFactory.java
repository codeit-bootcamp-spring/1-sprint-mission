package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.proxy.UserRepositoryProxy;

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
