package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFMessageRepository implements MessageRepository {

    private final Map<UUID, Message> data;

    public JCFMessageRepository() {
        data = new HashMap<>();
    }

    @Override
    public UUID save(Message message) {
        data.put(message.getId(), message);
        return message.getId();
    }

    @Override
    public Message findOne(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        List<Message> list = data.values().stream().filter(message -> message.getChannelId().equals(channelId))
                .toList();
        return new ArrayList<>(list);
    }

    @Override
    public UUID update(Message message){
        data.put(message.getId(), message);
        return message.getId();
    }

    @Override
    public UUID delete(UUID id) {
        data.remove(id);
        return id;
    }

    @Override
    public void deleteByChannelId(UUID channelId){
        findAllByChannelId(channelId)
                .forEach(message -> delete(message.getId()));
    }

}
