package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

//@Repository
public class FileMessageRepository implements MessageRepository {
    private final String FILE_PATH="message.ser";
    private final Map<UUID, Message> data;

    public FileMessageRepository() {
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
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save data to file: " + e.getMessage(), e);
        }
    }

    // 파일에서 데이터를 로드
    private Map<UUID, Message> loadDataFromFile() {
        File file = new File(FILE_PATH);
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
