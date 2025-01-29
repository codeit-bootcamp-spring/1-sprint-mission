package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.factory.type.repository.ChannelRepositoryType;
import com.sprint.mission.discodeit.factory.type.repository.UserRepositoryType;
import com.sprint.mission.discodeit.factory.type.service.ChannelServiceType;
import com.sprint.mission.discodeit.factory.type.service.UserServiceType;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

public class ChannelServiceFactory {
    public static ChannelService createService(ChannelServiceType serviceType, ChannelRepositoryType repositoryType) {
        return serviceType.create(repositoryType.create());
    }
}
