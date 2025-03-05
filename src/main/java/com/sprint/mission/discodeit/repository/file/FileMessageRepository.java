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
        List<Message> messages = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DIRECTORY.toFile()))) {
            while (true) {
                try {
                    Message existingMessage = (Message) ois.readObject();
                    messages.add(existingMessage);
                } catch (EOFException e) {
                    break;
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (FileNotFoundException e) {
            // 파일이 없는 경우, 새 파일 생성 후 저장
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 메시지 업데이트 또는 추가
        boolean messageUpdated = false;
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getId().equals(message.getId())) {
                messages.set(i, message);
                messageUpdated = true;
                break;
            }
        }
        if (!messageUpdated) {
            messages.add(message);
        }

        // 파일에 다시 쓰기
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DIRECTORY.toFile()))) {
            for (Message m : messages) {
                oos.writeObject(m);
            }
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