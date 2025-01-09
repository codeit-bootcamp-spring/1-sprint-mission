package com.srint.mission.discodeit.service.jcf;

import com.srint.mission.discodeit.entity.Message;
import com.srint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {

    private final Map<UUID, Message> data;

    public JCFMessageService() {
        this.data = new HashMap<>();
    }

    @Override
    public UUID create(Message message) {
        data.put(message.getId(), message);
        return message.getId();
    }

    @Override
    public Message read(UUID id) {
        Message message = data.get(id);
        return message;
    }

    @Override
    public List<Message> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Message update(UUID id, Message updatedMessage) {
        if(data.containsKey(id)){
            updatedMessage.setUpdatedAt(System.currentTimeMillis());
            data.put(id, updatedMessage);
            return updatedMessage;
        }
        throw new NoSuchElementException("not found");
    }

    @Override
    public UUID delete(UUID id) {
        Message message = data.remove(id);
        return message.getId();
    }
}
