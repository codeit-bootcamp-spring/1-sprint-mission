package com.sprint.mission.repository.file;

import com.sprint.mission.entity.main.Message;
import com.sprint.mission.repository.MessageRepository;
import com.sprint.mission.service.exception.NotFoundId;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FileMessageRepository implements MessageRepository {

    private static final Path MS_DIRECT_PATH = Path.of("MS_Directory");

    // 수정, 생성이 쓰는 메서드
    // 수정, 생성 오류 메시지 따로 설정하기 위해 throws

    @SneakyThrows
    @Override
    public void save(Message message) {
        Path msDirectPath = getMsDirectPath(message.getId());

        if (!Files.exists(msDirectPath)) {
            Files.createFile(msDirectPath);
        }

        ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(msDirectPath));
        oos.writeObject(message);
        oos.close();
    }


    @SneakyThrows
    @Override
    public Optional<Message> findById(UUID messageId) {
        return Optional.ofNullable(readMessageFromFile(getMsDirectPath(messageId)));
    }

    @SneakyThrows
    @Override
    public List<Message> findAll() {
        return Files.exists(MS_DIRECT_PATH)
                ? Files.list(MS_DIRECT_PATH)
                .filter(path -> path.toString().endsWith(".ser"))
                .map(this::readMessageFromFile)
                .collect(Collectors.toCollection(ArrayList::new))

                : new ArrayList<>();
    }

    @SneakyThrows
    public List<Message> findMessagesInChannel(UUID channelId) {
        return findAll().stream()
                .filter(message ->
                        message.getWrittenPlace().getId() == channelId)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @SneakyThrows
    @Override
    public void delete(UUID messageId) {
        Files.delete(getMsDirectPath(messageId));
    }


    /**
     * 편의 메서드
     */
    private Path getMsDirectPath(UUID id) {
        return MS_DIRECT_PATH.resolve(id + ".ser");
    }

    @SneakyThrows
    private Message readMessageFromFile(Path msPath) {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(msPath))) {
            return (Message) ois.readObject();
        }
    }

    /**
     * 메시지 디렉토리 생성
     */
    @SneakyThrows
    public void createDirectory() {
        if (Files.exists(MS_DIRECT_PATH)) {
            Files.list(MS_DIRECT_PATH).forEach(
                    file -> {
                        try {
                            Files.delete(file);
                        } catch (IOException e) {
                            throw new RuntimeException("삭제할 수 없는 Message파일이 있습니다.");
                        }
                    });
        } else {
            Files.createDirectory(MS_DIRECT_PATH);
        }
    }

}
