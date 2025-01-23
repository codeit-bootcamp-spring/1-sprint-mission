package com.sprint.mission.discodeit.service.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.notfound.NotfoundIdException;
import com.sprint.mission.discodeit.exception.validation.channel.InvalidChannelNameException;
import com.sprint.mission.discodeit.exception.validation.channel.InvalidChannelTypeException;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class FileChannelService implements ChannelService {
    private static final String CHANNEL_JSON_FILE = "tmp/channels.json";
    private final ObjectMapper mapper;
    private Map<UUID, Channel> channelMap;

    public FileChannelService() {
        mapper = new ObjectMapper();
        channelMap = new HashMap<>();
    }

    @Override
    public Channel create(String name, String description, ChannelType channelType) {
        if (name == null || name.isEmpty()) {
            throw new InvalidChannelNameException("유효하지 않은 채널명입니다.");
        }
        if (channelType == null || !isValidChannelType(channelType)) {
            throw new InvalidChannelTypeException("유효하지 않은 채널 타입입니다.");
        }
        Channel channel = new Channel(name, description, channelType);
        saveChannelToJson(channel);
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
    public Channel update(UUID channelId, String name, String description, ChannelType channelType) {
        if (!loadChannelFromJson().containsKey(channelId)) {
            throw new NotfoundIdException("유효하지 않은 Id입니다.");
        }
        if (name == null || name.isEmpty()) {
            throw new InvalidChannelNameException("유효하지 않은 채널명입니다.");
        }
        if (channelType == null || !isValidChannelType(channelType)) {
            throw new InvalidChannelTypeException("유효하지 않은 채널 타입입니다.");
        }

        Channel checkChannel = loadChannelFromJson().get(channelId);
        checkChannel.update(name, description, channelType);
        saveChannelToJson(checkChannel);
        return checkChannel;
    }

    @Override
    public void delete(UUID channelId) {
        channelMap = loadChannelFromJson();
        if (!channelMap.containsKey(channelId)) {
            throw new NotfoundIdException("유효하지 않은 Id입니다.");
        }
        channelMap.remove(channelId);
    }

    private boolean isValidChannelType(ChannelType channelType) {
        for (ChannelType type : ChannelType.values()) {
            if (type == channelType) {
                return true;
            }
        }
        return false;
    }

    private Map<UUID, Channel> loadChannelFromJson() {
        File file = new File(CHANNEL_JSON_FILE);
        if (!file.exists() || file.length() == 0) {
            return new HashMap<>();
        }
        try {
            return Arrays.asList(mapper.readValue(file, Channel[].class))
                    .stream()
                    .collect(Collectors.toMap(Channel::getId, channel -> channel));
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void saveChannelToJson(Channel channel) {
        channelMap = loadChannelFromJson();
        channelMap.put(channel.getId(), channel);
        saveAllChannelToJson(channelMap);
    }

    private void saveAllChannelToJson(Map<UUID, Channel> channels) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(CHANNEL_JSON_FILE), channels.values());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
