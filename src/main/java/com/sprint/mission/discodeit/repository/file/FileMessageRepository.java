package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "sprint-mission.repository.type", havingValue = "file")
public class FileMessageRepository implements MessageRepository {
    private static final String FILE_PATH = "files/messages.ser";
    private Map<UUID, Message> messages;

    public FileMessageRepository() {
        this.messages = loadFromFile();
    }

    @Override
    public Message save(Message message) {
        messages.put(message.getId(), message);
        saveToFile();
        return message;
    }

    @Override
    public Message findById(UUID messageId) {
        return messages.get(messageId);
    }

//    @Override
//    public Message findBySenderId(UUID senderId) {
//        for (Message message : messages.values()) {
//            if (message.getSenderId().equals(senderId)) {
//                return message;
//            }
//        }
//        return null;
//    }

    @Override
    public List<Message> findAllBySenderId(UUID senderId) {
        List<Message> result = new ArrayList<>();
        for (Message message : messages.values()) {
            if (message.getSenderId().equals(senderId)) {
                result.add(message);
            }
        }
        return result;
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        List<Message> result = new ArrayList<>();
        for (Message message : messages.values()) {
            if (message.getChannelId().equals(channelId)) {
                result.add(message);
            }
        }
        return result;
    }

    @Override
    public boolean existsById(UUID id) {
        return messages.containsKey(id);
    }

    @Override
    public void deleteById(UUID messageId) {
        messages.remove(messageId);
        saveToFile();
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        List<UUID> toDelete = new ArrayList<>();
        for (Map.Entry<UUID, Message> entry : messages.entrySet()) {
            if (entry.getValue().getChannelId().equals(channelId)) {
                toDelete.add(entry.getKey());
            }
        }
        for (UUID id : toDelete) {
            messages.remove(id);
        }
        saveToFile();
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<UUID, Message> loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (Map<UUID, Message>) ois.readObject();
        } catch (Exception e) {
            return new HashMap<>();
        }
    }
}