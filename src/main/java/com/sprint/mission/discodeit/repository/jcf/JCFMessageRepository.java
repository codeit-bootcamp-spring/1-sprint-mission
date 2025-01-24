package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID,Message> data;

    public JCFMessageRepository() {
        this.data = Collections.synchronizedMap(new HashMap<>());
    }

    @Override
    public void createMessage(Message message) {
        data.put(message.getId(),message);
    }

    @Override
    public Optional<Message> getMessageById(UUID id) {
        Message messageNullable=this.data.get(id);
        return Optional.ofNullable(Optional.ofNullable(messageNullable)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + id + " not found")));
    }

    @Override
    public List<Message> getAllMessage() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateMessage(UUID id, Message updatedMessage) {
        Message existingChannel = data.get(id);
        if (existingChannel != null) {
            existingChannel.update(updatedMessage.getContent());
        }
    }

    @Override
    public void deleteMessage(UUID id) {
        if(!this.data.containsKey(id)){
            throw new NoSuchElementException("Message with id "+id+" not found");
        }
        data.remove(id);
    }
}
