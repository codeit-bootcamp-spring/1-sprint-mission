package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.factory.BaseEntityFactory;
import com.sprint.mission.discodeit.factory.EntityFactory;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> messagesList;
    private static EntityFactory entityFactory;

    public JCFMessageService(EntityFactory entityFactory) {
        JCFMessageService.entityFactory = entityFactory;
        this.messagesList = new HashMap<>();
    }

    public JCFMessageService() {
        entityFactory = BaseEntityFactory.getInstance();
        this.messagesList = new HashMap<>();
    }

    @Override
    public Message createMessage(String title, String content, User sender, User receiver) {
        Message message = entityFactory.createMessage(title, content, sender, receiver);
        messagesList.put(message.getMessageId(), message);
        return message;
    }

    @Override
    public Message getMessageById(UUID messageId) {
        return messagesList.entrySet().stream()
                .filter(entry -> entry.getKey().equals(messageId))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElseThrow(() -> new IllegalArgumentException("찾는 메세지가 없습니다."));
    }

    @Override
    public Map<UUID, Message> getAllMessages() {
        return this.messagesList;
    }

    @Override
    public Message updateMessage(UUID messageId, String newTitle, String newContent) {
        Message message = getMessageById(messageId);
        message.updateMessage(newTitle, newContent);

        return message;
    }

    @Override
    public void deleteMessage(UUID messageId) {
        Message findMessage = getMessageById(messageId);
        messagesList.remove(findMessage.getMessageId());
    }
}
