package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileMessageRepository implements MessageRepository {

    private final Path DIRECTORY;

    public FileMessageRepository() {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), "Message.ser");
        if (Files.notExists(DIRECTORY)) {
            try {
                Files.createFile(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Message save(Message message) {
        try (
                FileOutputStream fos = new FileOutputStream(DIRECTORY.toFile(), true);
                ObjectOutputStream oos = new ObjectOutputStream(fos) {
                    @Override
                    protected void writeStreamHeader() throws IOException {
                        if (fos.getChannel().position() == 0) {
                            super.writeStreamHeader();
                        } else {
                            reset();
                        } //역직렬화 헤더 오류 해결 코드. 파일에 한번만 헤더 들어갈 수 있도록
                    }
                }
        ) {
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        List<Message> allContents = readAllContents();
        return allContents.stream()
                .filter(content -> content.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Message> findAllByIdIn(List<UUID> ids) {
        List<Message> allContents = readAllContents();
        return allContents.stream()
                .filter(content -> ids.contains(content.getId()))
                .toList();
    }


    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        List<Message> result = new ArrayList<>();
        if (Files.exists(DIRECTORY)) {
            try (
                    FileInputStream fis = new FileInputStream(DIRECTORY.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis) //try-with-resource
            ) {
                while (true) {
                    try {
                        Message message = (Message) ois.readObject();
                        if (message.getChannelId().equals(channelId)) {
                            result.add(message);
                        }
                    } catch (java.io.EOFException e) {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error reading Messages from file: " + e.getMessage());
                return new ArrayList<>();
            }
        }
        return result;
    }

    private List<Message> readAllContents() {
        List<Message> contents = new ArrayList<>();
        if (Files.exists(DIRECTORY)) {
            try (
                    FileInputStream fis = new FileInputStream(DIRECTORY.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
                while (true) {
                    try {
                        Message message = (Message) ois.readObject();
                        contents.add(message);
                    } catch (EOFException e) {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return contents;
    }

    @Override
    public boolean existsById(UUID id) {
        return findById(id).isPresent();
    }

    @Override
    public void deleteById(UUID id) {
        List<Message> allMessages = readAllContents();
        List<Message> updatedMessages = allMessages.stream()
                .filter(message -> !message.getId().equals(id))
                .toList();
        saveAllContents(updatedMessages);
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        this.findAllByChannelId(channelId)
                .forEach(message -> this.deleteById(message.getId()));
    }

    private void saveAllContents(List<Message> messages) {
        try (
                FileOutputStream fos = new FileOutputStream(DIRECTORY.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            for (Message message : messages) {
                oos.writeObject(message);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
