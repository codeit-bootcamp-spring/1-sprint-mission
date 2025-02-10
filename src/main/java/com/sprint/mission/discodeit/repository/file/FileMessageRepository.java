package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

//@Repository
public class FileMessageRepository implements MessageRepository {
    private final Path DIRECTORY;
    private final Path FILE_PATH;
    private final Map<UUID, Message> data;

    public FileMessageRepository(@Value("${discodeit.repository.file.storage-path}")String storagePath) {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), storagePath,"Message");
        this.FILE_PATH = DIRECTORY.resolve("message.ser");

        if (Files.notExists(DIRECTORY)) {
            try {
                Files.createDirectories(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create directory: " + DIRECTORY, e);
            }
        }
        this.data=loadDataFromFile();
    }

    @Override
    public Message save(Message message) {
        data.put(message.getId(),message);
        saveDataToFile();
        return message;
    }

    @Override
    public Optional<Message> getMessageById(UUID id) {
        return Optional.ofNullable(this.data.get(id));
    }

    @Override
    public List<Message> getAllMessage() {
        return new ArrayList<>(data.values());
    }

    @Override
    public boolean existsById(UUID id) {
        return this.data.containsKey(id);
    }


    @Override
    public void deleteMessage(UUID id) {
        if(!this.data.containsKey(id)){
            throw new NoSuchElementException("Message with id "+id+" not found");
        }
        data.remove(id);
        saveDataToFile();
    }

    @Override
    public Optional<Instant> findLastMessageTimeByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .map(Message::getCreatedAt)
                .max(Instant::compareTo);
    }

    @Override
    public List<Message> getMessagesByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        List<UUID> messageIds = data.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .map(Message::getId)
                .toList();

        if (messageIds.isEmpty()) {
            throw new NoSuchElementException("No messages found for channel ID: " + channelId);
        }

        messageIds.forEach(data::remove);
        saveDataToFile();
    }

    // 데이터를 파일에 저장
    private void saveDataToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH.toFile()))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save data to file: " + e.getMessage(), e);
        }
    }

    // 파일에서 데이터를 로드
    private Map<UUID, Message> loadDataFromFile() {
        File file = FILE_PATH.toFile();
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load data from file: " + e.getMessage(), e);
        }
    }
}
