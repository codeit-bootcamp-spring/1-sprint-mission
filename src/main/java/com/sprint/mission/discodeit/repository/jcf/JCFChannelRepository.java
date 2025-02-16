package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@Scope("singleton")
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> channels;

    public JCFChannelRepository() {
        this.channels = new HashMap<>();
    }

    @Override
    public Channel save(Channel channel) {
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
    public void delete(UUID channelId) {
        channels.remove(channelId);
    }

    @Override
    public boolean existsById(UUID channelId) {
        return channels.containsKey(channelId);
    }
}
