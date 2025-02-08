package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

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
        return data.get(id);
    }

    public List<Message> findAll() {
/*        if(data.isEmpty()){
            return Collections.emptyList(); // 빈 리스트 반환
        }*/
        return new ArrayList<>(data.values());
    }

    public UUID update(Message message){
        data.put(message.getId(), message);
        return message.getId();
    }

    public UUID delete(UUID id) {
        data.remove(id);
        return id;
    }
}
