package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> data;

    public JCFChannelRepository() {
        data = new HashMap<>(100);
    }

    @Override
    public Channel save(Channel channel) {
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID channelId) {
        return Optional.ofNullable(data.get(channelId));
    }

    @Override
    public List<Channel> findAllByUserId(UUID userId) {
        return data.values().stream()
                .filter(c -> {
                    try {
                        return c.getType() == Channel.Type.PUBLIC || c.getUser(userId) != null;
                    } catch (NotFoundException e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public void updateChannel(Channel channel) {
        data.put(channel.getId(), channel);
    }

    @Override
    public void deleteChannel(UUID channelId) {
        data.remove(channelId);
    }
}
