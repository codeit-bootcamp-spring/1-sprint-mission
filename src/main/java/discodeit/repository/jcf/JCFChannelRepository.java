package discodeit.repository.jcf;

import discodeit.entity.Channel;
import discodeit.entity.User;
import discodeit.repository.ChannelRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> channels;

    public JCFChannelRepository() {
        this.channels = new HashMap<>();
    }

    @Override
    public Channel save(String name, String introduction, User owner) {
        return null;
    }

    @Override
    public Channel find(UUID channelId) {
        return null;
    }

    @Override
    public List<Channel> findAll() {
        return List.of();
    }

    @Override
    public void update(UUID channelId, String name, String introduction) {

    }

    @Override
    public void delete(UUID channelId) {

    }
}
