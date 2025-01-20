package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class FileChannelRepository implements ChannelRepository {
    private final Path directory;
    private final AtomicLong idGenerator = new AtomicLong(1);

    public FileChannelRepository(Path directory) {
        this.directory = directory;
        init(directory);
    }

    private void init(Path directory) {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create directory: " + directory, e);
            }
        }
    }

    // 데이터 저장
    @Override
    public Long saveChannel(Channel channel) {
        Long id = idGenerator.getAndIncrement();
        Path filePath = directory.resolve(id + ".ser");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
            oos.writeObject(channel);
            return id;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save channel data: " + id, e);
        }
    }

    // 유저 데이터 로드
    @Override
    public Channel loadChannel(Long id) {
        Path filePath = directory.resolve(id + ".ser");
        if (!Files.exists(filePath)) {
            throw new RuntimeException("Channel data not found for ID: " + id);
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath.toFile()))) {
            return (Channel) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load channel data: " + id, e);
        }
    }

    @Override
    public Map<Long, Channel> loadAllChannels() {
        try {
            return Files.list(directory)
                    .filter(Files::isRegularFile)
                    .map(path -> {
                        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
                            Channel channel = (Channel) ois.readObject();
                            Long id = Long.valueOf(path.getFileName().toString().replace(".ser", ""));
                            return Map.entry(id, channel);
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException("Failed to load user data from file: " + path, e);
                        }
                    })
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load channels from directory", e);
        }
    }

    @Override
    public void deleteChannel(Long id) {
        try {
            Files.deleteIfExists(directory.resolve(id + ".ser"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateChannel(Long id, Channel channel) {
        Path filePath = directory.resolve(id + ".ser");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
            oos.writeObject(channel);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save channel data: " + id, e);
        }
    }
}
