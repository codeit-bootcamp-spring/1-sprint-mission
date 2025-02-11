package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        return messageList.get(id);
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
