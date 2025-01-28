package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FileChannelRepository implements ChannelRepository {
    private static final String CHANNEL_JSON_FILE = "tmp/channels.json";
    private final ObjectMapper mapper;
    private Map<UUID, Channel> channelMap;

    public FileChannelRepository() {
        mapper = new ObjectMapper();
        channelMap = new HashMap<>();
    }

    @Override
    public Channel save(Channel channel) {
        channelMap = loadChannelFromJson();
        channelMap.put(channel.getId(), channel);
        saveChannelToJson(channelMap);
        return channel;
    }

    @Override
    public Channel findById(UUID channelId) {
        return loadChannelFromJson().get(channelId);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(loadChannelFromJson().values());
    }

    @Override
    public void delete(UUID channelId) {
        channelMap = loadChannelFromJson();
        if (channelMap.containsKey(channelId)) {
            channelMap.remove(channelId);
            saveChannelToJson(channelMap);
        }
    }

    private Map<UUID, Channel> loadChannelFromJson() {
        Map<UUID, Channel> map = new HashMap<>();
        File file = new File(CHANNEL_JSON_FILE);
        if (!file.exists()) {
            return map;
        }
        try {
            map = mapper.readValue(file, new TypeReference<Map<UUID, Channel>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    private void saveChannelToJson(Map<UUID, Channel> channelMap) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(CHANNEL_JSON_FILE), channelMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
