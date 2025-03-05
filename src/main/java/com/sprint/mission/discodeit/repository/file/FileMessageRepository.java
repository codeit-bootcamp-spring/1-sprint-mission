package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FileMessageRepository implements MessageRepository {
    private final String filePath = "messages.dat";

    @Override
    public Message save(Message message) {
        Map<String, Message> messages = readFromFile();
        messages.put(message.getId(), message);
        writeToFile(messages);
        return message;
    }

    @Override
    public void deleteById(String id) {
        Map<String, Message> messages = readFromFile();
        messages.remove(id);
        writeToFile(messages);
    }

    @Override
    public Optional<Message> findById(String id) {
        Map<String, Message> messages = readFromFile();
        return Optional.ofNullable(messages.get(id));
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(readFromFile().values());
    }

    @Override
    public List<Message> findAllByChannelId(String channelId) {
        return readFromFile().values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .collect(Collectors.toList());
    }

    private Map<String, Message> readFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (Map<String, Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }

    private void writeToFile(Map<String, Message> messages) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}