package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.*;

public class JCFMessageService implements MessageService {
    private final Map<String, Message> data = new HashMap<>();

    @Override
    public Message create(Message message) {
        data.put(message.getId().toString(), message);
        return message;
    }

    @Override
    public Message findById(String id) {
        return data.get(id);
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Message update(String id, Message message) {
        if(data.containsKey(id)) {
            data.put(id, message);
            return message;
        }
        return null;
    }

    @Override
    public void delete(String id) {
        data.remove(id);
    }
}
