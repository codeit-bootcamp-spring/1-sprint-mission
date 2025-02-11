package com.sprint.mission.discodeit.fake;

import com.sprint.mission.discodeit.application.auth.PasswordEncoder;
import com.sprint.mission.discodeit.application.service.user.JCFUserService;
import com.sprint.mission.discodeit.application.service.user.converter.UserConverter;
import com.sprint.mission.discodeit.application.service.userstatus.UserStatusService;
import com.sprint.mission.discodeit.fake.repository.FakeUserRepository;
import com.sprint.mission.discodeit.repository.user.interfaces.UserRepository;
import com.sprint.mission.discodeit.repository.userstatus.UserStatusInMemoryRepository;
import com.sprint.mission.discodeit.repository.userstatus.interfaces.UserStatusRepository;

public class FakeFactory {
    private FakeFactory() {
    }

    public static UserRepository getUserRepository() {
        return new FakeUserRepository();
    }

    public static JCFUserService getUserService() {
        return new JCFUserService(
                getUserRepository(),
                new UserConverter(),
                new PasswordEncoder(),
                new UserStatusService(new UserStatusInMemoryRepository()));
    }
}
