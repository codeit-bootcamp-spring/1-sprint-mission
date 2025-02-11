package com.sprint.mission.repository.jcf.main;

import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.Message;
import com.sprint.mission.repository.MessageRepository;
import com.sprint.mission.service.exception.NotFoundId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class JCFMessageRepository implements MessageRepository{

    private final Map<UUID, Message> data = new TreeMap<>();

    @Override
    public Message save(Message message){
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID id){
        return Optional.ofNullable(data.get(id));
//        if (message == null) throw new NotFoundId("Cannot find Message : incorrect MessageId");
//        return message;
    }

    @Override
    public List<Message> findAll(){
        return new ArrayList<>(data.values());
    }


    public List<Message> findAllByChannel(Channel channel){
        return data.values().stream()
                .filter(message -> message.getWrittenPlace().equals(channel))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void delete(UUID messageId) {
        Message remove = data.remove(messageId);
        log.info("[Remove Message]  content : {}", remove.getContent());
    }

    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }
}
