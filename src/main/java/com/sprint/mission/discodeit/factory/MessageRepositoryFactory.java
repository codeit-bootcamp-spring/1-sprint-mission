package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.factory.type.repository.MessageRepositoryType;
import com.sprint.mission.discodeit.repository.MessageRepository;

public class MessageRepositoryFactory {
    public static MessageRepository createRepository(MessageRepositoryType repositoryType) {
        return repositoryType.create();
    }
}
