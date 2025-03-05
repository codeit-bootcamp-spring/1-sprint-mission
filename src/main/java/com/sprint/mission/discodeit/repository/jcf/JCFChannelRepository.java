package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Primary
@Repository
public class JCFChannelRepository implements ChannelRepository {
    private final Map<String, Channel> dataStore = new ConcurrentHashMap<>();

    @Override
    public Channel save(Channel channel) {
        dataStore.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public void deleteById(String id) {
        dataStore.remove(id);
    }

    @Override
    public Optional<Channel> findById(String id) {
        return Optional.ofNullable(dataStore.get(id));
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(dataStore.values());
    }
}