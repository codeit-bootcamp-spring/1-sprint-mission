package com.sprint.mission.discodeit.db.channel;

import com.sprint.mission.discodeit.db.common.CrudRepository;
import com.sprint.mission.discodeit.entity.channel.Channel;
import java.util.UUID;

public interface ChannelRepository extends CrudRepository<Channel, UUID> {

}
