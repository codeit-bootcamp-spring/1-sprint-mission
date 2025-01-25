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
        Map<UUID, Message> messages = findAllMessage();
        messages.put(message.getMessageId(), message);
        save(MESSAGE_PATH, messages);
        return message;
    }

    @Override
    public void removeMessageById(UUID messageId) {
        Map<UUID, Message> messages = findAllMessage();
        messages.remove(messageId);
        save(MESSAGE_PATH, messages);
        messageMap = messages;
    }

    @Override
    public Message findMessageById(UUID messageId) {
        messageMap = findAllMessage();  // 파일에서 최신 상태 로드
        Message message = messageMap.get(messageId);
        System.out.println("Finding message: " + message); // 디버깅용
        return message;
    }

    @Override
    public Map<UUID, Message> findAllMessage() {
        return load(MESSAGE_PATH, messageMap);
    }
}
