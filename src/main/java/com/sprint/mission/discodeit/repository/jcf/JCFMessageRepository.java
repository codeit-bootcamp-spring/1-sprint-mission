package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "repository.type", havingValue="jcf")
public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> data;

    public JCFMessageRepository() { this.data = new HashMap<>(); }

    @Override
    public boolean save(Message message) {
        data.put(message.getId(), message);
        return true;
    }

    @Override
    public Message findById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Message> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public List<Message> readAll(UUID id){
        List<Message> list =  data.values().stream()
                                        .filter(message -> message.getChannelId() != null && message.getChannelId().equals(id))
                                        .collect(Collectors.toList());

        return list;
    }

    @Override
    public Message modify(UUID id, Message modifiedMessage) {
        return data.replace(id, modifiedMessage);
    }

    @Override
    public boolean deleteById(UUID id) {
        try {
            data.remove(id);
            return true;
        } catch (NullPointerException e){
            System.out.println("유효하지 않은 메시지입니다.\n" + e);
        }
        return false;
    }
}
