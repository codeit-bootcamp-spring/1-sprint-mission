package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FileChannelRepository implements ChannelRepository {
    private static final String FILEPATH = "tmp/channel.ser";
    private final FileManager<Channel> fileManager =  new FileManager<>(FILEPATH);

    @Override
    public Channel save(Channel channel) {
        Map<UUID, Channel> savedChannelMap = loadChannelMapToFile();
        savedChannelMap.put(channel.getId(), channel);
        saveChannelMapToFile(savedChannelMap);
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID channelId) {
        Map<UUID, Channel> savedChannelMap = loadChannelMapToFile();
        return Optional.ofNullable(savedChannelMap.get(channelId));
    }

    @Override
    public List<Channel> findAll() {
        return loadChannelListToFile();
    }

    @Override
    public void delete(UUID channelId) {
        Map<UUID, Channel> savedChannelMap = loadChannelMapToFile();
        savedChannelMap.remove(channelId);
        saveChannelMapToFile(savedChannelMap);
    }

    private void saveChannelMapToFile(Map<UUID, Channel> saveChannelMap) {
        List<Channel> saveChannelList = saveChannelMap.values().stream().toList();
        fileManager.saveListToFile(saveChannelList);
    }

    private List<Channel> loadChannelListToFile() {
        return fileManager.loadListToFile();
    }

    private Map<UUID, Channel> loadChannelMapToFile() {
        List<Channel> loadChannelList = loadChannelListToFile();
        if (loadChannelList.isEmpty()) {
            return new HashMap<>();
        }
        return loadChannelList.stream()
                .collect(Collectors.toMap(Channel::getId, Function.identity()));
    }
}
