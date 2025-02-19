package some_path._1sprintmission.discodeit.repository.file;

import org.springframework.stereotype.Repository;
import some_path._1sprintmission.discodeit.dto.MessageUpdateRequestDTO;
import some_path._1sprintmission.discodeit.entiry.BaseEntity;
import some_path._1sprintmission.discodeit.entiry.Message;
import some_path._1sprintmission.discodeit.repository.MessageRepository;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class FileMessageRepository implements MessageRepository {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileMessageRepository() {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), "file-data-map", Message.class.getSimpleName());
        if (Files.notExists(DIRECTORY)) {
            try {
                Files.createDirectories(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private Path resolvePath(UUID id) {
        return DIRECTORY.resolve(id + EXTENSION);
    }

    @Override
    public Message save(Message message) {
        Path path = resolvePath(message.getId());
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return message;
    }

    @Override
    public Message update(MessageUpdateRequestDTO request) {
        Optional<Message> existingMessageOpt = findById(request.getMessageId());
        if (existingMessageOpt.isEmpty()) {
            throw new IllegalArgumentException("Message not found");
        }

        Message existingMessage = existingMessageOpt.get();

        // 새로운 Message 객체로 갱신
        Message updatedMessage = new Message(
                existingMessage.getChannel(),
                existingMessage.getSender(),
                request.getContent()
        );

        // 기존 ID 유지
        Field idField;
        try {
            idField = BaseEntity.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(updatedMessage, existingMessage.getId());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to update message ID", e);
        }

        // 저장
        return save(updatedMessage);
    }

    @Override
    public Optional<Message> findById(UUID id) {
        Message messageNullable = null;
        Path path = resolvePath(id);
        if (Files.exists(path)) {
            try (
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
                messageNullable = (Message) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return Optional.ofNullable(messageNullable);
    }

    @Override
    public List<Message> findAll() {
        try {
            return Files.list(DIRECTORY)
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try (
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis)
                        ) {
                            return (Message) ois.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsById(UUID id) {
        Path path = resolvePath(id);
        return Files.exists(path);
    }

    @Override
    public void deleteById(UUID messageId) {
        Path path = resolvePath(messageId);
        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete Message", e);
            }
        }
    }

    @Override
    public void deleteBySender(UUID senderId) {
        try (Stream<Path> files = Files.list(DIRECTORY)) {
            files.filter(path -> {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
                    Message message = (Message) ois.readObject();
                    return message.getSender().equals(senderId);
                } catch (IOException | ClassNotFoundException e) {
                    return false;
                }
            }).forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        Path path = resolvePath(channelId);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("메시지 삭제 실패", e);
        }
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        try (Stream<Path> files = Files.list(DIRECTORY)) {
            return files.map(path -> {
                try (
                        FileInputStream fis = new FileInputStream(path.toFile());
                        ObjectInputStream ois = new ObjectInputStream(fis)
                ) {
                    Message message = (Message) ois.readObject();
                    return message.getChannel().equals(channelId) ? message : null;
                } catch (IOException | ClassNotFoundException e) {
                    return null;
                }
            }).filter(Objects::nonNull).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to list Message files", e);
        }
    }
}

