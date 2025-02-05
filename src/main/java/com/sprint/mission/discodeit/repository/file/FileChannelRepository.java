package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class FileChannelRepository implements ChannelRepository {

    private final Path directory;

    public FileChannelRepository() {
        this.directory = Paths.get("src", "main", "resources", "data", "serialized", "channels");
    }

    @Override
    public Channel save(String name, String introduction, User owner) {
        Channel channel = new Channel(name, introduction, owner);
        Path filePath = directory.resolve(channel.getId() + ".ser");
        try (
                FileOutputStream fos = new FileOutputStream(filePath.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(channel);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
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
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();
            return channels;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Channel channel, String name, String introduction) {
        channel.update(name, introduction);
    }

    @Override
    public void delete(Channel channel) {
        Path filePath = directory.resolve(channel.getId() + ".ser");

        try {
            Files.delete(filePath);
        } catch (IOException e) {
            System.out.println("삭제에 실패하였습니다.");
        }
    }
}
