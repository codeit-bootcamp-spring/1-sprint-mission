package com.sprint.mission.repository.jcf.main;

import com.sprint.mission.entity.Message;
import com.sprint.mission.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFMessageRepository implements MessageRepository{

    // TreeSet 선택 1. 모든 채널의 메시지는 무수히 많을 것 2. 시간 순서 필요
    private final Map<UUID, Message> data = new HashMap<>();

    @Override
    public Message save(Message message){
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID id){
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Message> findAll(){
        return new ArrayList<>(data.values());
    }

    @Override
    public void delete(UUID messageId) {
        data.remove(messageId);
    }

    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }
}
