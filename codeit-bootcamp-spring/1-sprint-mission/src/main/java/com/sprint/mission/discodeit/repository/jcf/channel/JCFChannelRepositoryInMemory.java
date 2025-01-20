package com.sprint.mission.discodeit.repository.jcf.channel;

import com.sprint.mission.discodeit.repository.common.InMemoryCrudRepository;
import com.sprint.mission.discodeit.entity.channel.Channel;
import java.util.UUID;

public class JCFChannelRepositoryInMemory extends InMemoryCrudRepository<Channel, UUID> implements ChannelRepository  {
    private JCFChannelRepositoryInMemory() {}

    public static ChannelRepository getChannelRepositoryInMemory() {
        return new JCFChannelRepositoryInMemory();
    }


}
