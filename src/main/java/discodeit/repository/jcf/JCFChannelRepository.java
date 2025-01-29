package discodeit.repository.jcf;

import discodeit.enity.Channel;
import discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> data;

    public JCFChannelRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public void save(Channel channel) {
        data.put(channel.getId(), channel);
    }

    @Override
    public void delete(UUID channelId) {
        data.remove(channelId);
    }

    @Override
    public Channel findById(UUID channelId) {
        Channel channel = this.data.get(channelId);
        return Optional.ofNullable(channel)
                .orElseThrow(() -> new NoSuchElementException("Channel ID: " + channelId + " not found"));
    }

    public Map<UUID, Channel> findAll() {
        return new HashMap<>(data);
    }
}
