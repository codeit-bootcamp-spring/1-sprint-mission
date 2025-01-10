package com.spirnt.mission.discodeit.jcf;

import com.spirnt.mission.discodeit.entity.Message;
import com.spirnt.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data;

    public JCFMessageService(){
        this.data = new HashMap<>();
    }

    @Override
    public Message create(Message message) {
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Message read(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Message> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Message update(UUID id, Message updatedMessage) {
        if(data.containsKey(id)){
            Message exsitingMessage  = data.get(id);
            exsitingMessage.updateContent(updatedMessage.getContent());
            return  exsitingMessage;
        }
        return null;
    }

    @Override
    public boolean delete(UUID id) {
        return data.remove(id) != null;
    }
}
