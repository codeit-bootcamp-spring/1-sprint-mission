package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.file.FileService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FileChannelRepository implements ChannelRepository, FileService<Channel> {
    private static final String CHANNEL_PATH = "channel.ser";

    private Map<UUID, Channel> channelMap = new HashMap<>();

    @Override
    public Channel saveChannel(Channel channel) {
        Channel saveChannel = channelMap.put(channel.getChannelId(), channel);

        save(CHANNEL_PATH, channelMap);

        return channelMap.get(channel.getChannelId());
    }

    @Override
    public void removeChannelById(UUID channelId) {
        channelMap = findAllChannel();
        channelMap.remove(channelId);

        save(CHANNEL_PATH, channelMap);
    }

    @Override
    public Channel findChannelById(UUID channelId) {
        channelMap = findAllChannel();
        return channelMap.get(channelId);
    }

    @Override
    public Map<UUID, Channel> findAllChannel() {
        return load(CHANNEL_PATH, channelMap);
    }
}
