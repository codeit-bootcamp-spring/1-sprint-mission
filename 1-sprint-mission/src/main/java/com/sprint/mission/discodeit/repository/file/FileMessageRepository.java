package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository
public class FileMessageRepository implements MessageRepository {

    private static final String FILE_PATH = "tmp/message.ser";
    private final Map<User, List<Message>> messageData;


    public FileMessageRepository() {
        this.messageData = loadFromFile();
    }

    @Override
    public Message save(Message message) {
        messageData.computeIfAbsent(message.getUser(), k -> new ArrayList<>()).add(message);
        saveToFile();
        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(messageData.values().stream()
                .flatMap(List::stream)
                .filter(message -> message.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not found Message")));
    }

    @Override
    public List<Message> findAll() {
        List<Message> allMessages = new ArrayList<>();
        messageData.values().forEach(allMessages::addAll);
        return allMessages;
    }


    @Override
    public void deleteByMessage(Message message) {
        List<Message> messages = messageData.get(message.getUser());
        if(messages != null) {
            messages.remove(message);
            if(messages.isEmpty()) {
                messageData.remove(message.getUser());
            }
            saveToFile();
        }
    }


    @Override
    public boolean existsByChannel(Channel channel) {
        return messageData.values().stream()
                .flatMap(List::stream)
                .anyMatch(m -> m.getChannel().equals(channel));

    }

    @Override
    public boolean existsByUser(User user) {
        return messageData.values().stream()
                .flatMap(List::stream)
                .anyMatch(m -> m.getUser().equals(user));
    }

    private Map<User, List<Message>> loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(file))) {
            return (Map<User, List<Message>>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return new HashMap<>();
        }
    }

    private void saveToFile() {
        try (ObjectOutputStream oos
                     = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(messageData);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save message to file.", e);
        }

    }


}
