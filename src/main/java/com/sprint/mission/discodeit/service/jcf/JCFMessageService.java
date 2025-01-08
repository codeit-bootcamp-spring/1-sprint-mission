package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.BaseService;

import java.util.*;

public class JCFMessageService implements BaseService<Message> {
    private final Map<UUID, Message> data = new HashMap<>();

    @Override
    public Message create(Message message) {
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Message readById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Message> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Message update(UUID id, Message message) {
        try{
            Message checkMessage = data.get(id);
            if (checkMessage != null) {
                checkMessage.update(message.getContent());
            }
            return checkMessage;
        } catch (IllegalArgumentException e){
            System.out.println("유효하지 않는 id입니다.");
            return null;
        }
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}
