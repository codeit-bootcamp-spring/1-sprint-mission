package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.factory.type.repository.MessageRepositoryType;
import com.sprint.mission.discodeit.factory.type.repository.UserRepositoryType;
import com.sprint.mission.discodeit.factory.type.service.MessageServiceType;
import com.sprint.mission.discodeit.factory.type.service.UserServiceType;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

public class MessageServiceFactory {
    public static MessageService createService(MessageServiceType serviceType, MessageRepositoryType repositoryType) {
        return serviceType.create(repositoryType.create());
    }
}
