package com.sprint.mission.repository.jcf.main;

import com.sprint.mission.entity.main.Message;
import com.sprint.mission.repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class JCFMessageRepository implements MessageRepository{

    private final Map<UUID, Message> data = new TreeMap<>();

    @Override
    public void save(Message message){
        data.put(message.getId(), message);
    }

    @Override
    public Optional<Message> findById(UUID id){
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Message> findAll(){
        return new ArrayList<>(data.values());
    }


    public List<Message> findAllByChannel(UUID channelId){
        return data.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
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

    public void deleteAllByChannelId(UUID channelId) {
        data.values().removeIf(message -> message.getChannelId().equals(channelId));
    }
}
