package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;


public class FileChannelRepository implements ChannelRepository {
    public Path directory = Paths.get(System.getProperty("user.dir"), "data/channel");

    // 초기화
    public void init(Path directory) {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // 저장
    public boolean saveChannel(Channel channel) {
        init(directory); // 초기화
        Path filePath = directory.resolve(channel.getId().toString().concat(".ser"));
        if (Files.exists(filePath)) {
            deleteChannel(channel);
        }

        try(
                FileOutputStream fos = new FileOutputStream(filePath.toFile(), false);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(channel);
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 조회
    public Channel loadChannel(Channel channel) {
        Path filePath = directory.resolve(channel.getId().toString().concat(".ser"));

        if (Files.exists(filePath)) {
            try (
                    FileInputStream fis = new FileInputStream(filePath.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis);
            ) {
                return (Channel) ois.readObject();
            }  catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }

    public List<Channel> loadAllChannels() {
        if (Files.exists(directory)) {
            try {
                List<Channel> list = Files.list(directory)
                        .map(path -> {
                            try (
                                    FileInputStream fis = new FileInputStream(path.toFile());
                                    ObjectInputStream ois = new ObjectInputStream(fis)
                            ) {
                                return (Channel) ois.readObject();
                            } catch (IOException | ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .toList();
                return list;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return new ArrayList<>();
        }
    }

    // 삭제
    public boolean deleteChannel(Channel channel) {
        Path filePath = directory.resolve(channel.getId().toString().concat(".ser"));
        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
