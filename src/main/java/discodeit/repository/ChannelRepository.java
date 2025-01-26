package discodeit.repository;

import discodeit.entity.Channel;
import discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository {
    Channel save(String name, String introduction, User owner);
    Channel find(UUID channelId);
    List<Channel> findAll();
    void update(UUID channelId, String name, String introduction);
    void delete(UUID channelId);
}
