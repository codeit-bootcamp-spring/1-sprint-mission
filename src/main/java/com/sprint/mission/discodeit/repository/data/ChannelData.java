package com.sprint.mission.discodeit.repository.data;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// Channel 데이터만 관리하는 일급 컬렉션
public class ChannelData {

    private final Map<UUID, Channel> channels = new ConcurrentHashMap<>();
    private static ChannelData channelData;

    private ChannelData() {};

    public static ChannelData getInstance() {

        if (channelData == null) {
            channelData = new ChannelData();
        }

        return channelData;
    }

    public void save(Channel channel) {

        channels.put(channel.getId(), channel);
    }

    public Map<UUID, Channel> load() {

        return channels;
    }

    public void delete(UUID id) {

        channels.remove(id);
    }
}