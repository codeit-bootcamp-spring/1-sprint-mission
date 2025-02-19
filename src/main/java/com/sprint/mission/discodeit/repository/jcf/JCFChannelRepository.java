package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> data;
    public JCFChannelRepository(){
        this.data = new HashMap<>();
    }

    @Override
    public void save(Channel channel) {
        data.put(channel.getId(), channel);
    }

    @Override
    public Optional<Channel> findById(UUID channelId) {
        return Optional.ofNullable(data.get(channelId));
    }

    @Override
    public Map<UUID, Channel> findAll() {
        return new HashMap<>(data);
    }

    @Override
    public void delete(UUID channelId) {
        data.remove(channelId);
    }

    @Override
    public boolean existsById(UUID channelId) {
        return data.containsKey(channelId);
    }
}
