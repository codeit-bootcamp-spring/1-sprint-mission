package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.FileIOException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public class FileChannelService implements ChannelService {

    private static final FileChannelService fileChannelService = new FileChannelService();
    private static final Path filePath;
    private final UserService userService;

    static {
        filePath = Path.of(System.getProperty("user.dir"), "channels");
        createDirectory();
    }

    private FileChannelService() {
        userService = FileUserService.getInstance();
    }

    public static FileChannelService getInstance() {
        return fileChannelService;
    }

    private static void createDirectory() {
        if (!Files.exists(FileChannelService.filePath)) {
            try {
                Files.createDirectories(FileChannelService.filePath);
            } catch (IOException e) {
                throw new FileIOException("저장 디렉토리 생성 실패: " + filePath);
            }
        }
    }

    @Override
    public Channel createChannel(ChannelDto channelDto) {
        return writeChannelToFile(Channel.of(channelDto.getType(), channelDto.getName(), channelDto.getDescription()));
    }

    private Channel writeChannelToFile(Channel channel) {
        Path path = filePath.resolve(channel.getId().toString().concat(".ser"));

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            oos.writeObject(channel);
            return channel;
        } catch (IOException e) {
            throw new FileIOException("channel 저장 실패");
        }
    }

    @Override
    public Channel readChannel(UUID channelId) {
        Path path = filePath.resolve(channelId.toString().concat(".ser"));

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            return (Channel) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new NotFoundException("등록되지 않은 channel입니다.");
        }
    }

    @Override
    public List<Channel> readAll() {
        try {
            return Files.list(filePath)
                    .map(path -> {
                        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
                            Object data = ois.readObject();
                            return (Channel) data;
                        } catch (IOException | ClassNotFoundException e) {
                            throw new FileIOException("channels 읽기 실패");
                        }
                    })
                    .toList();
        } catch (IOException e) {
            throw new FileIOException("channels 읽기 실패");
        }
    }

    @Override
    public void updateChannel(UUID channelId, ChannelDto channelDto) {
        Channel channel = readChannel(channelId);
        channel.updateType(channelDto.getType());
        channel.updateName(channelDto.getName());
        channel.updateDescription(channelDto.getDescription());
        writeChannelToFile(channel);
    }

    @Override
    public void addUser(UUID channelId, UUID userId) {
        Channel channel = readChannel(channelId);
        User user = userService.readUser(userId);
        channel.addUser(user);
        writeChannelToFile(channel);
    }

    @Override
    public void deleteUser(UUID channelId, UUID userId) {
        Channel channel = readChannel(channelId);
        channel.deleteUser(userId);
        writeChannelToFile(channel);
    }

    @Override
    public void deleteChannel(UUID channelId) {
        Path path = filePath.resolve(channelId.toString().concat(".ser"));

        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new FileIOException("channel 삭제 실패");
            }
        }
    }
}
