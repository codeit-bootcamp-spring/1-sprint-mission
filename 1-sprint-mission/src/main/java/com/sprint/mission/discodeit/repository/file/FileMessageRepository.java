package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.interfacepac.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public class FileMessageRepository implements MessageRepository {


    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final String filePath;
    private final Map<UUID, List<Message>> messageData;

    public FileMessageRepository(@Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory) {
        if (!fileDirectory.endsWith("/")) {
            fileDirectory += "/";
        }
        this.filePath = fileDirectory + "message.json";
        log.info("***** FileMessageRepository CONSTRUCTOR CALLED *****"); // 활성화 됐는지 확인
        ensureDirectoryExists(this.filePath);
        this.messageData = loadFromFile();
    }

    @Override
    public Message save(Message message) {
        UUID userId = message.getUser().getId();
        messageData.computeIfAbsent(userId, k -> new ArrayList<>()).add(message);
        saveToFile();
        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return messageData.values().stream()
                .flatMap(List::stream)
                .filter(msg -> msg.getId().equals(id))
                .findFirst();

    }

    @Override
    public List<Message> findAll() {
        return messageData.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
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
        UUID userId = message.getUser().getId();
        List<Message> messages = messageData.get(userId);
        if (messages != null) {
            messages.remove(message);
            if (messages.isEmpty()) {
                messageData.remove(userId);
            }
            saveToFile();
        }
    }

    @Override
    public void deleteByChannel(Channel channel) {
        messageData.values().forEach(messages ->
                messages.removeIf(message -> message.getChannel().equals(channel)));
        saveToFile();
    }

    @Override
    public void deleteById(UUID messageId) {
        messageData.values().forEach(messages ->
                messages.removeIf(message -> message.getId().equals(messageId)));
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
        UUID userId = user.getId();
        return messageData.containsKey(userId);
    }



    private void ensureDirectoryExists(String fileDirectory) {
        File directory = new File(fileDirectory);
        File parentDir = directory.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
    }

    private Map<UUID, List<Message>> loadFromFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ConcurrentHashMap<>();
        }
        try {
            return objectMapper.readValue(
                    file,
                    new TypeReference<Map<UUID, List<Message>>>() {}
            );
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return new ConcurrentHashMap<>();
        }
    }

    private void saveToFile() {
        File file = new File(filePath);
        try {
            objectMapper.writeValue(file, messageData);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save message to file.", e);
        }
    }

}
