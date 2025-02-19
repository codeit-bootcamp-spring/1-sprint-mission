package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class FileMessageRepository  implements MessageRepository {
    private static final String FILE_PATH = "files/messages.ser";
    private List<Message> messages;

    public FileMessageRepository() {
        this.messages = loadFromFile();
    }

    @Override
    public void save(Message message) {
        messages.add(message);
        saveToFile();
    }

    @Override
    public Message findByMessageId(UUID messageId) {
        for (Message message : messages) {
            if (message.getId().equals(messageId)) {
                return message;
            }
        }
        return null;
    }

    @Override
    public List<Message> findBySenderId(UUID senderId) {
        List<Message> result = new ArrayList<>();
        for (Message message : messages) {
            if (message.getSenderId().equals(senderId)) {
                result.add(message);
            }
        }
        return result;
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        List<Message> result = new ArrayList<>();
        for (Message message : messages) {
            if (message.getChannelId().equals(channelId)) {
                result.add(message);
            }
        }
        return result;
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messages);
    }

    @Override
    public void deleteById(UUID messageId) {
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getId().equals(messageId)) {
                messages.remove(i);
                break;
            }
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

    private List<Message> loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (List<Message>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

}
