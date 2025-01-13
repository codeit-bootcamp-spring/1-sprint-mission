package com.sprint.mission.discodeit.factory.jcf;

import com.sprint.mission.discodeit.factory.UserServiceFactory;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.service.proxy.UserServiceProxy;

public class JCFUserServiceFactory implements UserServiceFactory {
    @Override
    public UserService createUserService() {
        return new UserServiceProxy(new JCFUserService());
    }
}
