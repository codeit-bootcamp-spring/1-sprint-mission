package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@Primary
public class FileChannelRepository implements ChannelRepository {
    private static final String FILE_PATH = "tmp/channel.ser";
    private final FileManager<Channel> fileManager =  new FileManager<>(FILE_PATH);

    @Override
    public Channel save(Channel channel) {
        Map<UUID, Channel> savedChannelMap = loadChannelMapToFile();
        savedChannelMap.put(channel.getId(), channel);
        saveChannelMapToFile(savedChannelMap);
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        Map<UUID, Channel> savedChannelMap = loadChannelMapToFile();
        return Optional.ofNullable(savedChannelMap.get(id));
    }

    @Override
    public List<Channel> findAll() {
        return fileManager.loadListToFile();
    }

    @Override
    public void deleteById(UUID id) {
        Map<UUID, Channel> savedChannelMap = loadChannelMapToFile();
        savedChannelMap.remove(id);
        saveChannelMapToFile(savedChannelMap);
    }

    private void saveChannelMapToFile(Map<UUID, Channel> saveChannelMap) {
        List<Channel> saveChannelList = saveChannelMap.values().stream().collect(Collectors.toList());
        fileManager.saveListToFile(saveChannelList);
    }

    private Map<UUID, Channel> loadChannelMapToFile() {
        List<Channel> loadChannelList = fileManager.loadListToFile();
        if (loadChannelList.isEmpty()) {
            return new HashMap<>();
        }
        return loadChannelList.stream()
                .collect(Collectors.toMap(Channel::getId, Function.identity()));
    }
}
