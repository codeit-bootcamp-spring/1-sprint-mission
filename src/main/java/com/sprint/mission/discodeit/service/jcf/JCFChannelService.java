package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JCFChannelService implements ChannelService {
    private static volatile JCFChannelService instance;
    private final Map<UUID, Channel> data;

    private JCFChannelService() {
        this.data = new ConcurrentHashMap<>(); // data 동시성 관리
    }

    protected static JCFChannelService getInstance() {
        if (instance == null) {
            synchronized (JCFChannelService.class) {
                if (instance == null) {
                    instance = new JCFChannelService();
                }
            }
        }
        return instance;
    }

    @Override
    public Channel createChannel(String name, String topic, ChannelType type) {
        Channel channel = new Channel(name, topic, type);
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Optional<Channel> getChannelDetails(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Channel> findAllChannels() {
        return new ArrayList<>(data.values());
    }

    @Override
    public boolean editChannel(UUID id, String name, String topic, ChannelType type) {
        return data.computeIfPresent(id, (key, channel) -> {
            channel.update(name, topic, type);
            return channel;
        }) != null;
    }

    @Override
    public boolean deleteChannel(UUID id) {
        return data.remove(id) != null;
    }
}