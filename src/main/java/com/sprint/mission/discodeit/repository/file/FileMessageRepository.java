package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.Message;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.util.FileIOUtil.*;
import static com.sprint.mission.discodeit.util.FileIOUtil.saveToFile;

public class FileMessageRepository implements MessageRepository {
    private final Path filePath = Path.of("./result/messages.ser");
    private final Map<UUID, Message> messageMap;

    public FileMessageRepository() {
        if (!Files.exists(this.filePath)) {
            try {
                Files.createFile(this.filePath);
                saveToFile(new HashMap<>(), this.filePath);
            } catch (IOException e) {
                throw new RuntimeException("메시지 파일을 초기화 하던 중에 문제가 발생했습니다", e);
            }
        }
        this.messageMap = loadFromFile(this.filePath);
    }

    @Override
    public Message save(Message message) {
        messageMap.put(message.getId(), message);
        saveToFile(messageMap, filePath);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID uuid) {
        return Optional.of(messageMap.get(uuid));
    }

    @Override
    public List<Message> findByUser(User user) {
        return messageMap.values().stream()
                .filter(message -> message.getWriter().getPhone().equals(user.getPhone()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> findByChannel(Channel channel) {
        return messageMap.values().stream()
                .filter(message -> message.getChannel().getName().equals(channel.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> findAll() {
        return messageMap.values().stream()
                .collect(Collectors.toList());
    }

    @Override
    public Message delete(Message message) {
        messageMap.remove(message.getId());
        saveToFile(messageMap, filePath);
        return message;
    }
}
