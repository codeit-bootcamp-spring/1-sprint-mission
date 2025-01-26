package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFMessageRepository implements MessageRepository {
    private final List<Message> messages = new ArrayList<>();

    @Override
    public void save(Message message) {
        messages.add(message);
    }

    @Override
    public Message findByMessageId(UUID messageId) {
        for (Message message : messages) {
            if (message.getId().equals(messageId)) {
                return message;
            }
        }
        return null;
    }

    @Override
    public List<Message> findbyUsername(String username){
        List<Message> result = new ArrayList<>();
        for (Message message : messages) {
            if (message.getUsername().equals(username)) {
                result.add(message);
            }
        }
        return result;
    }

    @Override
    public List<Message> findAll() {
        return messages;
    }

    @Override
    public void deleteById(UUID id){
        for(int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getId().equals(id)) {
                messages.remove(i);
                return;
            }
        }
    }
}
