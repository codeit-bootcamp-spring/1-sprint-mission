package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.FileIOException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public class FileMessageRepository implements MessageRepository {


    private static final FileMessageRepository fileMessageRepository = new FileMessageRepository();
    private static final Path filePath;
    private final ChannelRepository channelRepository;

    static {
        filePath = Path.of(System.getProperty("user.dir"), "messages");
        createDirectory();
    }

    private FileMessageRepository() {
        channelRepository = FileChannelRepository.getInstance();
    }

    public static FileMessageRepository getInstance() {
        return fileMessageRepository;
    }

    private static void createDirectory() {
        if (!Files.exists(FileMessageRepository.filePath)) {
            try {
                Files.createDirectories(FileMessageRepository.filePath);
            } catch (IOException e) {
                throw new FileIOException("저장 디렉토리 생성 실패: " + filePath);
            }
        }
    }
    @Override
    public Message save(Message message) {
        Path path = filePath.resolve(message.getId().toString().concat(".ser"));

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            oos.writeObject(message);
            return message;
        } catch (IOException e) {
            throw new FileIOException("message 저장 실패");
        }
    }

    @Override
    public Message findMessage(UUID messageId) {
        Path path = filePath.resolve(messageId.toString().concat(".ser"));

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            return (Message) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new NotFoundException("등록되지 않은 message입니다.");
        }
    }

    @Override
    public List<Message> findAll() {
        try {
            return Files.list(filePath)
                    .map(path -> {
                        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
                            Object data = ois.readObject();
                            return (Message) data;
                        } catch (IOException | ClassNotFoundException e) {
                            throw new FileIOException("messages 읽기 실패");
                        }
                    })
                    .toList();
        } catch (IOException e) {
            throw new FileIOException("messages 읽기 실패");
        }
    }

    @Override
    public void updateMessage(Message message) {
        save(message);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        Path path = filePath.resolve(messageId.toString().concat(".ser"));

        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new FileIOException("message 삭제 실패");
            }
        }
    }
}
