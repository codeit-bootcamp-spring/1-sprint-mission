package com.sprint.mission.repository.jcf.main;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.Message;
import com.sprint.mission.repository.MessageRepository;
import com.sprint.mission.service.exception.NotFoundId;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class JCFMessageRepository implements MessageRepository{

    private final Map<UUID, Message> data = new TreeMap<>();

    @Override
    public Message save(Message message){
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Message findById(UUID id){
        Message message = data.get(id);
        if (message == null) throw new NotFoundId("Cannot find Message : incorrect MessageId");
        return message;
    }

    @Override
    public List<Message> findAll(){
        return new ArrayList<>(data.values());
    }


    public List<Message> findAllByChannelId(Channel channel){
        return data.values().stream()
                .filter(message -> message.getWrittenPlace().equals(channel))
                .collect(Collectors.toCollection(ArrayList::new));
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
