package com.srint.mission.discodeit.repository.jcf;

import com.srint.mission.discodeit.entity.Message;
import com.srint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {

    private final Map<UUID, Message> data;

    public JCFMessageRepository() {
        data = new HashMap<>();
    }

    public UUID save(Message message) {
        data.put(message.getId(), message);
        return message.getId();
    }

    public Message findOne(UUID id) {
        if(!data.containsKey(id)){
            throw new IllegalArgumentException("조회할 Message을 찾지 못했습니다.");
        }
        return data.get(id);
    }

    public List<Message> findAll() {
        if(data.isEmpty()){
            return Collections.emptyList(); // 빈 리스트 반환
        }
        return data.values().stream().toList();
    }

    @Override
    public UUID update(Message message) {
        data.put(message.getId(), message);
        return message.getId();
    }

    public UUID delete(UUID id) {
        if(!data.containsKey(id)){
            throw new IllegalArgumentException("삭제할 Message를 찾지 못했습니다.");
        }
        data.remove(id);
        return id;
    }
}
