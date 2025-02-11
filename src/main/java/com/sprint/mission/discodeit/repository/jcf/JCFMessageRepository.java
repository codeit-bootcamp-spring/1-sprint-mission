package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

public class JCFMessageRepository implements MessageRepository {

    private final Map<UUID, Message> messageList;

    public JCFMessageRepository() {
        this.messageList = new HashMap<>();
    }


    @Override
    public Message save(Message message) {
        messageList.put(message.getId(), message);
        return message;
    }


    @Override
    public Message findById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID가 null입니다.");
        }

        Message message = messageList.get(id);
        if (message == null) {
            throw new NoSuchElementException("해당 객체가 존재하지 않습니다: " + id);
        }

        return message;
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        List<Message> messageFindByChannelList = messageList.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();
        return messageFindByChannelList;
    }

    @Override
    public Map<UUID, Message> load() {
        if (messageList.isEmpty()) {
            throw new IllegalStateException("메시지 목록이 초기화되지 않았습니다.");
        }
        return messageList;
    }

    @Override
    public void delete(UUID id) {
        messageList.remove(id);
    }

    @Override
    public void deleteByChannelId(UUID id) {
        List<Message> toDeleteMessageList = findByChannelId(id);
        for (Message toDeleteMessage : toDeleteMessageList) {
            messageList.remove(id);
        }
    }
}
