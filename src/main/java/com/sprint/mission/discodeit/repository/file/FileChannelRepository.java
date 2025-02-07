package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.exception.FileIOException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class FileChannelRepository implements ChannelRepository {

    private static final Path filePath;
    private final String FILE_EXTENSION = ".ser";

    static {
        filePath = Path.of(System.getProperty("user.dir"), "channels");
        FileManager.createDirectory(filePath);
    }

    @Override
    public Channel save(Channel channel) {
        Path path = filePath.resolve(channel.getId().toString().concat(FILE_EXTENSION));

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            oos.writeObject(channel);
            return channel;
        } catch (IOException e) {
            throw new FileIOException("channel 저장 실패");
        }
    }

    @Override
    public Channel findById(UUID channelId) {
        Path path = filePath.resolve(channelId.toString().concat(FILE_EXTENSION));

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            return (Channel) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new NotFoundException("등록되지 않은 channel입니다.");
        }
    }

    @Override
    public List<Channel> findAll() {
        File[] files = filePath.toFile().listFiles();
        List<Channel> channels = new ArrayList<>(100);

        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Channel channel = (Channel) ois.readObject();
                channels.add(channel);
            } catch (IOException | ClassNotFoundException e) {
                throw new FileIOException("channels 읽기 실패");
            }
        }
        return channels;
    }

    @Override
    public void updateChannel(Channel channel) {
        save(channel);
    }

    @Override
    public void deleteChannel(UUID channelId) {
        Path path = filePath.resolve(channelId.toString().concat(FILE_EXTENSION));

        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new FileIOException("channel 삭제 실패");
            }
        }
    }
}