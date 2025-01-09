package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data;
    public JCFChannelService() {
        this.data = new HashMap<>();
    }

    @Override
    public Channel create(Channel channel) {
        if (data.containsKey(channel.getId())) {
            throw new IllegalArgumentException("Channel Id already exists: " + channel.getId());
        }
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Optional<Channel> read(Channel channel) {
        return Optional.ofNullable(data.get(channel.getId()));
    }

    @Override
    public List<Channel> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Channel update(Channel existChannel, Channel updateChannel) {
        // 기존 Channel 객체가 존재하는지 확인
        if (!data.containsKey(existChannel.getId())) {
            throw new NoSuchElementException("Channel not found");
        }
        updateChannel.updateTime();
        data.put(existChannel.getId(), updateChannel);
        return updateChannel;
    }

    @Override
    public boolean delete(Channel channel) {
        if (!data.containsKey(channel.getId())) {
            return false;
        }
        data.remove(channel.getId());
        return true;
    }

}
