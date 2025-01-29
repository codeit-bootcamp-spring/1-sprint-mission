package com.sprint.mission.discodeit.factory.type.repository;

import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.proxy.UserRepositoryProxy;

import java.util.function.Supplier;

public enum UserRepositoryType {
    JCF(JCFUserRepository::new),
    FILE(FileUserRepository::new)
    ;

    private final Supplier<UserRepository> supplier;

    UserRepositoryType(Supplier<UserRepository> supplier) {
        this.supplier = supplier;
    }

    public UserRepository create() {
        return new UserRepositoryProxy(supplier.get());
    }
}
