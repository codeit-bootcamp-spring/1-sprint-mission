package com.sprint.mission.discodeit.db.channel;

import com.sprint.mission.discodeit.db.common.InMemoryCrudRepository;
import com.sprint.mission.discodeit.entity.channel.Channel;
import java.util.UUID;

public class ChannelRepositoryInMemory extends InMemoryCrudRepository<Channel, UUID> implements ChannelRepository  {
    private ChannelRepositoryInMemory() {}

    public static ChannelRepository getChannelRepositoryInMemory() {
        return new ChannelRepositoryInMemory();
    }
}
