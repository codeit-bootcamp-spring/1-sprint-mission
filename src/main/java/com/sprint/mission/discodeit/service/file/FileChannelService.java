package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.ServiceException;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.util.ChannelType;
import com.sprint.mission.discodeit.util.FileIOUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.util.FileIOUtil.*;
import static com.sprint.mission.discodeit.util.FileIOUtil.saveToFile;

public class FileChannelService implements ChannelService {
    private final Path filePath = Path.of("./result/channels.ser");
    private final Map<UUID, Channel> channels = loadFromFile(filePath);

    public FileChannelService() {
        if (!Files.exists(this.filePath)) {
            try {
                Files.createFile(this.filePath);
                saveToFile(new HashMap<>(), this.filePath);
            } catch (IOException e) {
                throw new RuntimeException("채널 파일을 초기화 하던 중에 문제가 발생했습니다", e);
            }
        }
    }

    @Override
    public Channel create(String name, String description, ChannelType channelType) {
        if (channels.values().stream()
                .anyMatch(user -> user.getName().equals(name))) {
            throw new ServiceException(ErrorCode.DUPLICATE_CHANNEL);
        }
        Channel createChannel = new Channel(name, description, channelType);
        channels.put(createChannel.getId(), createChannel);
        saveToFile(channels, filePath);
        return createChannel;
    }

    @Override
    public Optional<Channel> getChannelByName(String name) {
        for (Channel channel : channels.values()) {
            if (channel.getName().equals(name)) {
                return Optional.of(channel);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Channel> getAllChannel() {
        return channels.values().stream()
                .collect(Collectors.toList());
    }

    @Override
    public Channel updateType(Channel channel, ChannelType channelType) {
        validateChannel(channel);
        channel.changeType(channelType);
        channels.put(channel.getId(), channel);
        FileIOUtil.saveToFile(channels, filePath);
        return channel;
    }

    @Override
    public Channel updateDescription(Channel channel, String description) {
        validateChannel(channel);
        channel.changeDescription(description);
        channels.put(channel.getId(), channel);
        FileIOUtil.saveToFile(channels, filePath);
        return channel;
    }


    @Override
    public void delete(Channel channel) {
        validateChannel(channel);
        channels.remove(channel.getId());
        saveToFile(channels, filePath);
    }

    private void validateChannel(Channel channel) {
        Channel findChannel = getChannelByName(channel.getName())
                .orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_CHANNEL));
    }
}