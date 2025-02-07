package com.sprint.mission.discodeit.fake;

import com.sprint.mission.discodeit.application.service.UserService;
import com.sprint.mission.discodeit.application.service.converter.UserConverter;
import com.sprint.mission.discodeit.fake.repository.FakeUserRepository;
import com.sprint.mission.discodeit.repository.user.interfaces.UserRepository;

public class FakeFactory {
    private FakeFactory() {
    }

    public static UserRepository getUserRepository() {
        return new FakeUserRepository();
    }

    public static UserService getUserService() {
        return new UserService(getUserRepository(), new UserConverter());
    }
}
