package com.sprint.mission.repository.file;

import com.sprint.mission.entity.main.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository

public class FileChannelRepository {
    private static final Path CHANNEL_DIRECT_PATH = Path.of("channelDirectory");

    //@Override
    @SneakyThrows
    public Channel save(Channel channel) {
        Path channelPath = getChannelDirectPath(channel.getId());

        if (!Files.exists(channelPath)) {
            Files.createFile(channelPath);
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(channelPath))) {
            oos.writeObject(channel);
            return channel;
        }
    }


    //@Override
    @SneakyThrows
    public Optional<Channel> findById(UUID id) {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(getChannelDirectPath(id)))) {
            return Optional.ofNullable((Channel) ois.readObject());
        }
    }

    //@Override
    @SneakyThrows
    public List<Channel> findAll() {
        return Files.exists(CHANNEL_DIRECT_PATH)
                ? Files.list(CHANNEL_DIRECT_PATH)
                .filter(path -> path.toString().endsWith(".ser"))
                .map((path) -> readChannelFromFile(path))
                .collect(Collectors.toCollection(ArrayList::new))
                : new ArrayList<>();
    }

    //@Override
    @SneakyThrows
    public void delete(Channel channel) {
        Files.delete(getChannelDirectPath(channel.getId()));
    }

    /**
     * 편의 메서드
     */
    private Path getChannelDirectPath(UUID id) {
        return CHANNEL_DIRECT_PATH.resolve(id.toString() + ".ser");
    }


    public Channel readChannelFromFile(Path channelPath) {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(channelPath))) {
            return (Channel) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 디렉토리 생성 - 기존 디렉토리의 파일들 삭제 (테스트용)
     */
    @SneakyThrows
    public void createChannelDirectory() {

        if (Files.exists(CHANNEL_DIRECT_PATH)) {
            Files.list(CHANNEL_DIRECT_PATH)
                    .forEach(file -> {
                        try {
                            Files.delete(file);
                        } catch (IOException e) {
                            throw new RuntimeException("삭제할 수 없는 Channel 파일이 있습니다");
                        }
                    });
            // Files.delete(CHANNEL_DIRECT_PATH); 굳이 디렉토리까지 삭제할 필요가 없다
        } else {
            Files.createDirectory(CHANNEL_DIRECT_PATH);
        }
    }
}

