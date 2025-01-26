package discodeit.service;

import discodeit.entity.Channel;
import discodeit.entity.Message;
import discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel create(String name, String introduction, User owner);
    Channel find(UUID channelId);
    List<Channel> findAll();
    String getInfo(UUID channelId);
    void update(UUID channelId, String name, String introduction);
    void delete(UUID channelId);
}
