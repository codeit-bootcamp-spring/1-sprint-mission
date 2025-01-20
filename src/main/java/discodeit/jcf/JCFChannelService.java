package discodeit.jcf;

import discodeit.ChannelType;
import discodeit.entity.Channel;
import discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data;

    public JCFChannelService() {
        this.data = new HashMap<>();
    }

    @Override
    public Channel create(String name, String description, ChannelType type) {
        Channel channel = new Channel(name, description, type);
        data.put(channel.getId(), channel);

        return channel;
    }

    @Override
    public Channel find(UUID channelId) {
        Channel channel = data.get(channelId);

        return Optional.ofNullable(channel)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
    }

    @Override
    public List<Channel> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public Channel update(UUID channelId, String name, String description, ChannelType type) {
        Channel channelNullable = data.get(channelId);
        Channel channel = Optional.ofNullable(channelNullable)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
        channel.update(name, description, type);

        return channel;
    }

    @Override
    public void delete(UUID id) {
        if(!data.containsKey(id)) {
            throw new NoSuchElementException("Channel with id " + id + " not found");
        }
        data.remove(id);
    }
}
