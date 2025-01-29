package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFMessageRepository implements MessageRepository {

    private final Map<UUID, Message> messagelList;

    public JCFMessageRepository() {
        this.messagelList = new HashMap<>();
    }

    @Override
    public Message save(Message message) {
        messagelList.put(message.getMsgId(), message);
        return message;
    }

    @Override
    public Map<UUID, Message> load() {
        return messagelList;
    }

    @Override
    public void delete(UUID id) {
        messagelList.remove(id);
    }
}
