package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.interfacepac.MessageRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileMessageRepository implements MessageRepository {

    private static final String FILE_PATH = "tmp/entity/message.ser";
    private static final ObjectMapper objectMapper = new ObjectMapper();
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
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageData.values().stream()
                .flatMap(List::stream)
                .filter(message -> message.getChannel().getId().equals(channelId))
                .collect(Collectors.toList());
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
    public void deleteByChannel(Channel channel) {
        messageData.values()
                .forEach(messages -> messages.removeIf(message -> message.getChannel().equals(channel)));
        saveToFile();
    }

    @Override
    public void deleteById(UUID messageId) {
        messageData.values()
                .forEach(messages
                        -> messages.removeIf(message -> message.getId().equals(messageId)));
        saveToFile();
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
        try {
            return objectMapper.readValue(file, new TypeReference<Map<User, List<Message>>>() {});
        } catch (IOException e) {
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
