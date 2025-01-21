package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.util.FileIOUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.util.FileIOUtil.loadFromFile;
import static com.sprint.mission.discodeit.util.FileIOUtil.saveToFile;

public class FileChannelRepository implements ChannelRepository {
    private final Path filePath;
    private final Map<UUID, Channel> channelMap;

    public FileChannelRepository(String filePath) {
        this.filePath = Paths.get(filePath);
        if (!Files.exists(this.filePath)) {
            try {
                Files.createFile(this.filePath);
                saveToFile(new HashMap<>(), this.filePath);
            } catch (IOException e) {
                throw new RuntimeException("채널 파일을 초기화 하던 중에 문제가 발생했습니다", e);
            }
        }
        this.channelMap = loadFromFile(this.filePath);
    }

    @Override
    public Channel save(Channel channel) {
        channelMap.put(channel.getId(), channel);
        saveToFile(channelMap, filePath);
        return channel;
    }

    @Override
    public Optional<Channel> getByName(String channelName) {
        return channelMap.values().stream()
                .filter(channel -> channel.getName().equals(channelName))
                .findFirst();
    }

    @Override
    public List<Channel> getByUser(User user) {
        return channelMap.values().stream()
                .filter(channel -> channel.getMembers().stream()
                        .anyMatch(member -> Objects.equals(member.getPhone(), user.getPhone()))
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<Channel> getAll() {
        return channelMap.values().stream()
                .collect(Collectors.toList());
    }

    @Override
    public Channel delete(Channel channel) {
        channelMap.remove(channel.getId());
        saveToFile(channelMap, filePath);
        return channel;
    }
}
