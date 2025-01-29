package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> data = new HashMap<>();

    @Override
    public void createChannel(Channel channel) {
        data.put(channel.getId(), channel);
    }

    @Override
    public Optional<Channel> getChannel(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Channel> getAllChannels() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateChannel(UUID id, String channelName) {
        Channel channel = data.get(id);
        if (channel == null) {
            throw new IllegalArgumentException("해당 id의 채널을 찾을 수 없습니다: " + id);
        }
        channel.update(channelName);
    }

    @Override
    public void deleteChannel(UUID id) {
        data.remove(id);
    }
}
