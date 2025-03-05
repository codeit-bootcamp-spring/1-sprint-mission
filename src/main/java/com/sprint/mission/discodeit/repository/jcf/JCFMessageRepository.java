package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
@Repository
@ConditionalOnProperty(name="discodeit.repository.type", havingValue = "jcf")
public class JCFMessageRepository implements MessageRepository {
    private final HashMap<String, Message> data;

    public JCFMessageRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public Message save(Message message) {
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public boolean delete(String messageId) {
        return data.remove(messageId) != null;
    }

    @Override
    public Message findById(String id) {
        return data.get(id);
    }

    @Override
    public List<Message> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public List<Message> findAllByChannelId(String channelId) {
        return data.values().stream().filter(m -> m.getChannelId().equals(channelId)).toList();
    }
}
