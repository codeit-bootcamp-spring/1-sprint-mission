package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.validator.ChannelValidator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class FileChannelService implements ChannelService {

    private final ChannelValidator validator;
    private final Path directory;

    public FileChannelService() {
        this.validator = new ChannelValidator();
        this.directory = Paths.get("src", "main", "resources", "data", "serialized", "channels");
    }

    @Override
    public Channel create(String name, String introduction, User owner) {
        validator.validate(name, introduction);
        Channel channel = new Channel(name, introduction, owner);
        Path filePath = directory.resolve(channel.getId() + ".ser");
        try (
                FileOutputStream fos = new FileOutputStream(filePath.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(channel);
        } catch (IOException e) {
            throw new RuntimeException("[ERROR] 채널 생성에 실패하였습니다.");
        }
        return channel;
    }

    @Override
    public Channel find(UUID channelId) {
        Path filePath = directory.resolve(channelId + ".ser");
        try (
                FileInputStream fis = new FileInputStream(filePath.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            Object data = ois.readObject();
            return (Channel) data;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("[ERROR] 채널을 찾을 수 없습니다.");
        }
    }

    @Override
    public List<Channel> findAll() {
        try {
            List<Channel> channels = Files.list(directory)
                    .map(path -> {
                        try (
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis);
                        ) {
                            Object data = ois.readObject();
                            return (Channel) data;
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException("[ERROR] 채널을 찾을 수 없습니다.");
                        }
                    })
                    .toList();
            return channels;
        } catch (IOException e) {
            throw new RuntimeException("[ERROR] 채널을 찾을 수 없습니다.");
        }
    }

    @Override
    public String getInfo(UUID channelId) {
        Channel channel = find(channelId);
        return channel.toString();
    }

    @Override
    public void update(UUID channelId, String name, String introduction) {
        validator.validate(name, introduction);
        Channel channel = find(channelId);
        channel.update(name, introduction);

        Path filePath = directory.resolve(channelId + ".ser");
        try (
                FileOutputStream fos = new FileOutputStream(filePath.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(channel);
        } catch (IOException e) {
            throw new RuntimeException("[ERROR] 채널 업데이트에 실패하였습니다.");
        }
    }

    @Override
    public void delete(UUID channelId) {
        Path filePath = directory.resolve(channelId + ".ser");

        try {
            Files.delete(filePath);
        } catch (IOException e) {
            System.out.println("[ERROR] 채널 삭제에 실패하였습니다.");
        }
    }
}
