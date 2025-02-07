package com.sprint.mission.discodeit.repository.jcf.message.ChannelMessage;

import com.sprint.mission.discodeit.repository.common.InMemoryCrudRepository;
import com.sprint.mission.discodeit.entity.message.ChannelMessage;
import java.util.UUID;

public class JCFChannelMessageRepositoryInMemory extends InMemoryCrudRepository<ChannelMessage, UUID> implements ChannelMessageRepository {

    private JCFChannelMessageRepositoryInMemory() {}

    public static JCFChannelMessageRepositoryInMemory getInstance() {
        return new JCFChannelMessageRepositoryInMemory();
    }
}
