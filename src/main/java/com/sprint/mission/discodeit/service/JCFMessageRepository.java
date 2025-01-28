package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.HashMap;
import java.util.UUID;

public class JCFMessageRepository implements MessageRepository {
    private final HashMap<UUID, Message> data = new HashMap<>();

    @Override
    public void save(Message message) {
        data.put(message.getId(), message);
        System.out.println("메세지 저장 완료 : " + message.getId());
    }

    @Override
    public void findById(UUID id) {
        if (data.containsKey(id)) {
            System.out.println("message Id : " + data.get(id).getId());
            System.out.println("message content : " + data.get(id).getMessageContent());
        }
    }

    @Override
    public void findAll() {
        for (UUID uuid : data.keySet()) {
            System.out.println("message Id : " + data.get(uuid).getId());
            System.out.println("message content : " + data.get(uuid).getMessageContent());
        }
    }

    @Override
    public void update(Message message) {
        data.put(message.getId(), message);
        message.updateUpdatedAt(System.currentTimeMillis());
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}
