package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data;

    public JCFMessageService(){
        this.data = new HashMap<>();
    }

    @Override
    public Message save(Message message) {
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> read(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Message> readAll(){
        return new ArrayList<>(data.values());
    }

    @Override
    public Message update(UUID id, Message message){
        if(!data.containsKey(id)){
            return null;
        }
        data.put(id, message);
        message.setUpdatedAt(System.currentTimeMillis());
        return message;
    }

    @Override
    public boolean delete(UUID id) {
        if(!data.containsKey(id)){
            return false;
        }
        data.remove(id);
        return true;
    }
}
