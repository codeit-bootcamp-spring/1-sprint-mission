package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

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
    public Channel findById(UUID channelId) {
        Channel channel = this.data.get(channelId);
        return Optional.ofNullable(channel).orElseThrow(() -> new NoSuchElementException(channelId + "를 찾을 수 없습니다."));
    }

    @Override
    public void delete(UUID channelId) {
        data.remove(channelId);
    }

    @Override
    public Map<UUID, Channel> findAll() {
        return new HashMap<>(data);
    }
}
