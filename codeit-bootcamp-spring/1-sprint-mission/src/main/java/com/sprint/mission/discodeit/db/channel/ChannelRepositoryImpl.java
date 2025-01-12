package com.sprint.mission.discodeit.db.channel;

import com.sprint.mission.discodeit.db.common.InMemoryCrudRepository;
import com.sprint.mission.discodeit.entity.channel.Channel;
import java.util.UUID;

public class ChannelRepositoryImpl extends InMemoryCrudRepository<Channel, UUID> implements ChannelRepository  {
}
