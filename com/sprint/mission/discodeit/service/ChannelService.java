package sprint.mission.discodeit.service;

import sprint.mission.discodeit.entity.Channel;

import java.util.UUID;

public interface ChannelService {
    Channel create(Channel channelToCreate);
    Channel read(UUID key);
    Channel update(UUID key, Channel channelToUpdate);
    Channel delete(UUID key);
}
