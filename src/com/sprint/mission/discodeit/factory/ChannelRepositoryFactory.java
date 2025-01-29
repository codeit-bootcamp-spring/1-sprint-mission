package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.factory.type.repository.ChannelRepositoryType;
import com.sprint.mission.discodeit.repository.ChannelRepository;

public class ChannelRepositoryFactory {
    public static ChannelRepository createRepository(ChannelRepositoryType repositoryType) {
        return repositoryType.create();
    }
}
