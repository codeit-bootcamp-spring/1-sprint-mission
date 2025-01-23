package com.sprint.mission.discodeit.service.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.notfound.NotfoundIdException;
import com.sprint.mission.discodeit.exception.validation.message.InvalidMessageException;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class FileMessageService implements MessageService {
    private static final String MESSAGE_JSON_FILE = "tmp/messages.json";
    private final ObjectMapper mapper;
    private Map<UUID, Message> messageMap;

    public FileMessageService() {
        mapper = new ObjectMapper();
        messageMap = new HashMap<>();
    }

    @Override
    public Message create(String content, UUID authorId, UUID channelId) {
        if (content == null || content.isEmpty()) {
            throw new InvalidMessageException("유효하지 않은 문장입니다.");
        }
        Message message = new Message(content, authorId, channelId);
        saveMessageToJson(message);
        return message;
    }

    @Override
    public Message findById(UUID contentId) {
        return loadMessageFromJson().get(contentId);
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(loadMessageFromJson().values());
    }

    @Override
    public Message update(UUID contentId, String content) {
        if (content == null || content.isEmpty()) {
            throw new InvalidMessageException("유효하지 않은 문장입니다.");
        }
        Message checkMessage = loadMessageFromJson().get(contentId);
        checkMessage.update(content);
        saveMessageToJson(checkMessage);
        return checkMessage;
    }

    @Override
    public void delete(UUID contentId) {
        messageMap = loadMessageFromJson();
        if (!loadMessageFromJson().containsKey(contentId)) {
            throw new NotfoundIdException("유효하지 않은 Id입니다.");
        }
        messageMap.remove(contentId);
    }

    private Map<UUID, Message> loadMessageFromJson() {
        File file = new File(MESSAGE_JSON_FILE);
        if (!file.exists() || file.length() == 0) {
            return new HashMap<>();
        }
        try {
            return Arrays.asList(mapper.readValue(file, Message[].class))
                    .stream()
                    .collect(Collectors.toMap(Message::getId, message -> message));
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void saveMessageToJson(Message message) {
        messageMap = loadMessageFromJson();
        messageMap.put(message.getId(), message);
        saveAllMessageToJson(messageMap);
    }

    private void saveAllMessageToJson(Map<UUID, Message> messages) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(MESSAGE_JSON_FILE), messages.values());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
