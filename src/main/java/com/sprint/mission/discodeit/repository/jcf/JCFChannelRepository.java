package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

@Profile("jcf")
@Repository
public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> channelMap;

    public JCFChannelRepository() {
        this.channelMap = new HashMap<>();
    }

    @Override
    public Channel save(Channel channel) {
        channelMap.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public boolean channelExistById(UUID channelID) {
        return channelMap.containsKey(channelID);
    }

    @Override
    public Optional<Channel> findById(UUID channelId) {
        return channelMap.values().stream()
                .filter(channel -> channel.getId().equals(channelId))
                .findFirst();
    }

    @Override
    public List<Channel> findAll() {
        return channelMap.values().stream().toList();
    }

    @Override
    public Channel delete(Channel channel) {
        channelMap.remove(channel.getId());
        return channel;
    }
}
