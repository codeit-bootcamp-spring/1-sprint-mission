package com.sprint.mission.discodeit.repository.data;

import com.sprint.mission.discodeit.entity.Message;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// Message 데이터만 관리하는 일급 컬렉션
public class MessageData {

    private final Map<UUID, Message> messages = new ConcurrentHashMap<>();
    private static MessageData messageData;

    private MessageData() {
    }

    public static MessageData getInstance() {

        if (messageData == null) {
            messageData = new MessageData();
        }

        return messageData;
    }

    public void save(Message message) {

        messages.put(message.getId(), message);
    }

    public Map<UUID, Message> load() {

        return messages;
    }

    public void delete(UUID id) {

        messages.remove(id);
    }
}