package discodeit.service;

import discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    void create(Channel channel);
    Channel read(UUID id);
    List<Channel> readAll();
    void update (UUID id, String channelName);
    void delete(UUID id);
}
