package discodeit.service;

import discodeit.entity.Channel;

import java.util.UUID;

public interface ChannelService {
    Channel create();
    Channel read(UUID key);
    Channel update(UUID key, Channel channelToUpdate);
    Channel delete(UUID key);
}
