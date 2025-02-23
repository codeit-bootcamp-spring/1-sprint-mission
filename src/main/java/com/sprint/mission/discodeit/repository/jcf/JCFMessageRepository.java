package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFMessageRepository implements MessageRepository {

    private final Map<UUID, Message> data;

    public JCFMessageRepository() {
        data = new HashMap<>(100);
    }

    @Override
    public Message save(Message message) {
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID messageId) {
        return Optional.ofNullable(data.get(messageId));
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(m -> m.getChannel().getId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public void updateMessage(Message message) {
        data.put(message.getId(), message);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        data.remove(messageId);
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        findByChannelId(channelId)
                .forEach(m -> deleteMessage(m.getId()));
    }
}
