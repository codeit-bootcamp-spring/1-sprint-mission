package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.factory.jcf.JCFUserServiceFactory;

public enum UserServiceFactoryType {
    JCF(new JCFUserServiceFactory()),
    ;

    private final UserServiceFactory factory;

    UserServiceFactoryType(UserServiceFactory factory) {
        this.factory = factory;
    }

    public UserServiceFactory getFactory() {
        return factory;
    }
}
