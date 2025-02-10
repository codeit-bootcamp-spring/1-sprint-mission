package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.factory.type.repository.MessageRepositoryType;
import com.sprint.mission.discodeit.factory.type.service.MessageServiceType;
import com.sprint.mission.discodeit.service.MessageService;

public class MessageServiceFactory {
    public static MessageService createService(MessageServiceType serviceType, MessageRepositoryType repositoryType) {
        return serviceType.create(repositoryType.create());
    }
}
