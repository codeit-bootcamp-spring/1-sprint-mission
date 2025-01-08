package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data = new HashMap<>();

    @Override
    public void createChannel(String name) {
        Channel channel = new Channel(name);
        data.put(channel.getId(), channel);
    }

    @Override
    public UUID getChannel(UUID id) {
        return data.get(id).getId();

    }

    @Override
    public List<Channel> getAllChannels() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateChannel(UUID id, String name) {
        Channel channel = data.get(id);
        if(channel!= null) {
            channel.updateName(name);
        } else {
            throw new NoSuchElementException("Channel을 찾을 수 없습니다");
        }

    }
    @Override
    public void deleteChannel(UUID id) {
        if(data.containsKey(id)) {
            data.remove(id);
        } else {
            throw new NoSuchElementException("Channel을 찾을 수 없습니다.");
        }

    }
}
