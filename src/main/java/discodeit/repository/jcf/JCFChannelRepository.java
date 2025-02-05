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
        Channel channel = new Channel(name, introduction, owner);
        channels.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel find(UUID channelId) {
        return channels.get(channelId);
    }

    @Override
    public List<Channel> findAll() {
        return channels.values().stream().toList();
    }

    @Override
    public void update(Channel channel, String name, String introduction) {
        channel.update(name, introduction);
    }

    @Override
    public void delete(Channel channel) {
        channels.remove(channel.getId());
    }
}
