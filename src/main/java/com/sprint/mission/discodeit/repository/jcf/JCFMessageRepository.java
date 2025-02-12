package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> messages;

    public JCFMessageRepository(){
        this.messages = new HashMap<>();
    }

    @Override
    public Message save(Message message){
        this.messages.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(this.messages.get(id));
    }

    @Override
    public List<Message> findAll() {
        return this.messages.values().stream()
                .toList();
    }

    @Override
    public boolean existsId(UUID id) {
        return this.messages.containsKey(id);
    }

    @Override
    public void delete(UUID id){
        this.messages.remove(id);
    }

}
