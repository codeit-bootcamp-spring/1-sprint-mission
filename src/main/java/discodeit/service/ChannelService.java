package discodeit.service;

import discodeit.ChannelType;
import discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel create(String name, String description, ChannelType type);
    Channel find(UUID channelId);
    List<Channel> findAll();
    Channel update (UUID channelId, String name, String description, ChannelType type);
    void delete(UUID channelId);
}
