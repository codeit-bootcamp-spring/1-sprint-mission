package com.sprint.mission.discodeit.db.message.ChannelMessage;

import com.sprint.mission.discodeit.db.common.InMemoryCrudRepository;
import com.sprint.mission.discodeit.entity.message.ChannelMessage;
import java.util.UUID;

public class ChannelMessageRepositoryInMemory extends InMemoryCrudRepository<ChannelMessage, UUID> implements ChannelMessageRepository {

    private ChannelMessageRepositoryInMemory() {}

    public static ChannelMessageRepositoryInMemory getInstance() {
        return new ChannelMessageRepositoryInMemory();
    }
}
