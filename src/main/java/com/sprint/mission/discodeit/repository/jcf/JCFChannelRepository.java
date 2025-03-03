package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository("jcfChannelRepository")
public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> channels = new ConcurrentHashMap<>();

    @Override
    public Channel save(Channel channel) {
        channels.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID channelId) {
        return Optional.ofNullable(channels.get(channelId));
    }

    @Override
    public void deleteById(UUID channelId) {
        channels.remove(channelId);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channels.values());
    }

    @Override
    public List<Channel> findAllPrivateChannelsByUserId(UUID userId) {
        return channels.values().stream()
                .filter(channel -> channel.isPrivate() && channel.getMembers().contains(userId))
                .toList();
    }

    @Override
    public List<Channel> findAllChannelsForUser(UUID userId) {
        return channels.values().stream()
                .filter(channel -> channel.isPublic() || channel.getMembers().contains(userId))
                .toList();
    }

    @Override
    public boolean existsById(UUID id) {
        return channels.containsKey(id);
    }
}
