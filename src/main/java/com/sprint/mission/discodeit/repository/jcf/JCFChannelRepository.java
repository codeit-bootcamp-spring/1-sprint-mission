package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jsf")
public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> data = new HashMap<>();
    private final Map<UUID, Set<UUID>> privateChannelUsers = new HashMap<>();

    @Override
    public Channel save(Channel channel) {
        data.put(channel.getId(), channel);
        if (channel.getType() == ChannelType.PRIVATE) {
            privateChannelUsers.putIfAbsent(channel.getId(), new HashSet<>());
        }
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
        privateChannelUsers.remove(id);
    }

    @Override
    public void delete(Channel channel) {
        deleteById(channel.getId());
    }

    @Override
    public List<Channel> findByType(ChannelType channelType) {
        return data.values().stream()
                .filter(channel -> channel.getType() == channelType)
                .collect(Collectors.toList());
    }

    @Override
    public List<Channel> findByUserId(UUID userId) {
        return data.values().stream()
                .filter(channel -> channel.getType() == ChannelType.PUBLIC || isUserInPrivateChannel(channel.getId(), userId))
                .collect(Collectors.toList());
    }

    private boolean isUserInPrivateChannel(UUID channelId, UUID userId) {
        return privateChannelUsers.getOrDefault(channelId, Collections.emptySet()).contains(userId);
    }
}
