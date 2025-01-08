package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.*;

public class JCFMessageService implements MessageService{
    private final Map<UUID, Message> data = new HashMap<>();
    @Override
    public void createMessage(String content, UUID userId, UUID channelId) {
        Message message = new Message(content, userId, channelId);
        data.put(message.getId(), message);
    }

    @Override
    public Optional<Message> getMessage(UUID id) {
        return Optional.empty();
    }

    @Override
    public List<Message> getAllMessages() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateMessage(UUID id, String content) {
        Message message = data.get(id);
        if(message != null) {
            message.updateContent(content);
        } else {
            throw new NoSuchElementException("Message를 찾을 수 없습니다");
        }

    }

    @Override
    public void deleteMessage(UUID id) {
        if(data.containsKey(id)) {
            data.remove(id);
        } else {
            throw new NoSuchElementException("Message를 찾을 수 없습니다.");
        }

    }
}
