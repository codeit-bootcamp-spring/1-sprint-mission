package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.ArrayList;
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
    public List<Message> findBySenderId(UUID senderId) {
        List<Message> result = new ArrayList<>();
        for (Message message : messages) {
            if (message.getSenderId().equals(senderId)) {
                result.add(message);
            }
        }
        return result;
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        List<Message> result = new ArrayList<>();
        for (Message message : messages) {
            if (message.getChannelId().equals(channelId)) {
                result.add(message);
            }
        }
        return result;
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messages);
    }

    @Override
    public void deleteById(UUID messageId) {
        messages.removeIf(message -> message.getId().equals(messageId));
    }
}
