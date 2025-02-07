package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFMessageRepository implements MessageRepository {

    private final Map<UUID, Message> messageList;

    public JCFMessageRepository() {
        this.messageList = new HashMap<>();
    }

    @Override
    public Message findById(UUID id) {
        return messageList.get(id);
    }

    @Override
    public Message save(Message message) {
        messageList.put(message.getMsgId(), message);
        return message;
    }

    @Override
    public Map<UUID, Message> load() {
        return messageList;
    }

    @Override
    public void delete(UUID id) {
        messageList.remove(id);
    }
}
