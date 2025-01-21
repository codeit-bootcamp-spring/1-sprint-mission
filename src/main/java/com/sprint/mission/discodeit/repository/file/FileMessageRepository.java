package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.file.FileService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FileMessageRepository implements MessageRepository, FileService<Message> {
    private static final String MESSAGE_PATH = "message.ser";

    Map<UUID, Message> messageMap = new HashMap<>();

    @Override
    public Message saveMessage(Message message) {
        messageMap = findAllMessage();
        messageMap.put(message.getMessageId(), message);

        save(MESSAGE_PATH, messageMap);

        return message;
    }

    @Override
    public void removeMessageById(UUID messageId) {
        messageMap = findAllMessage();
        messageMap.remove(messageId);

        save(MESSAGE_PATH, messageMap);
    }

    @Override
    public Message findMessageById(UUID messageId) {
        return messageMap.get(messageId);
    }

    @Override
    public Map<UUID, Message> findAllMessage() {
        return load(MESSAGE_PATH, messageMap);
    }
}
