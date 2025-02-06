package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.exception.FileIOException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FileChannelRepository implements ChannelRepository {

    private static final Path filePath;

    static {
        filePath = Path.of(System.getProperty("user.dir"), "channels");
        FileManager.createDirectory(filePath);
    }

    @Override
    public Channel save(Channel channel) {
        Path path = filePath.resolve(channel.getId().toString().concat(".ser"));

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            oos.writeObject(channel);
            return channel;
        } catch (IOException e) {
            throw new FileIOException("channel 저장 실패");
        }
    }

    @Override
    public Channel findChannel(UUID channelId) {
        Path path = filePath.resolve(channelId.toString().concat(".ser"));

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            return (Channel) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new NotFoundException("등록되지 않은 channel입니다.");
        }
    }

    @Override
    public List<Channel> findAll() {
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
    public void updateChannel(Channel channel) {
        save(channel);
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