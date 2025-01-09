package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data = new HashMap<>();

    @Override
    public void create(Message message, Channel channel) {
        data.put(message.getId(), message);
        channel.getMessages().add(message);
    }

    @Override
    public Message read(UUID messageId) {
        return data.get(messageId);
    }

    @Override
    public List<Message> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void update(UUID messageId, String messageContent) {
        Message message = data.get(messageId);
        if (message != null) {
            message.update(messageContent);
            data.put(messageId, message);
        }
    }

    @Override
    public void delete(UUID messageId, Channel channel) {
        data.remove(messageId);
        channel.getMessages().removeIf(m -> m.getId().equals(messageId));
    }
}
