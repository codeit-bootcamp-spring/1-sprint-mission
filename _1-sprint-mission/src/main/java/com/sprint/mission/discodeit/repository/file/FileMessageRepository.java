package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.entity.Message;

import java.io.*;
import java.util.*;
public class FileMessageRepository implements MessageRepository {

    private final File filePath;
    private final Map<UUID, Message> messageData;

    public FileMessageRepository(File filePath) {
        this.filePath = filePath;
        this.messageData = loadMessageData();
    }

    // 파일에서 데이터 로드
    private Map<UUID, Message> loadMessageData() {
        if (!filePath.exists()) return new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (Map<UUID, Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }

    // 데이터를 파일에 저장
    private void saveMessageData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(messageData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Message create(Message message) {
        messageData.put(message.getId(), message);
        saveMessageData();
        return message;
    }

    @Override
    public Message findById(UUID messageId) {
        return messageData.get(messageId);
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messageData.values());
    }

    @Override
    public void delete(UUID messageId) {
        if (!messageData.containsKey(messageId)) {
            throw new NoSuchElementException("Message with ID " + messageId + " not found");
        }
        messageData.remove(messageId);
        saveMessageData();
    }
}
