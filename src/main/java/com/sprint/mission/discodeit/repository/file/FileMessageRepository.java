package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
import java.util.*;
import java.util.stream.Collectors;

@Primary
@Repository
public class FileMessageRepository implements MessageRepository {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileMessageRepository() {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), "file-data-map", "Message");
        if (Files.notExists(this.DIRECTORY)) {
            try {
                Files.createDirectories(this.DIRECTORY, new FileAttribute<?>[0]);
            } catch (IOException e) {
                throw new RuntimeException("메시지 저장소 디렉토리 생성에 실패했습니다.", e);
            }
        }
    }

    private Path resolvePath(UUID id) {
        return DIRECTORY.resolve(id.toString() + EXTENSION);
    }

    @Override
    public Message save(Message message) {
        Path path = resolvePath(message.getId());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException("메시지 저장에 실패했습니다.", e);
        }
        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        Path path = resolvePath(id);
        if (!Files.exists(path)) {
            return Optional.empty();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            return Optional.of((Message) ois.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("메시지 조회에 실패했습니다.", e);
        }
    }

    @Override
    public List<Message> findAll() {
        try {
            return Files.list(DIRECTORY)
                    .map(path -> {
                        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
                            return (Message) ois.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException("메시지 목록 조회 중 오류 발생", e);
                        }
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("메시지 목록 조회에 실패했습니다.", e);
        }
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return findAll().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        try {
            Files.deleteIfExists(resolvePath(id));
        } catch (IOException e) {
            throw new RuntimeException("메시지 삭제에 실패했습니다.", e);
        }
    }
}
