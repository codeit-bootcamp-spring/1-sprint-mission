package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.collection.Messages;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFMessageRepository implements MessageRepository {
    private final Messages messages = new Messages();

    @Override
    public Message save(Message message) {
        messages.add(message.getId(), message);
        return message;
    }

    @Override
    public List<Message> getAllMessages() {
        return messages.getMessagesList();
    }

    @Override
    public Message getMessageById(UUID uuid) {
        return messages.get(uuid).orElse(null);
    }

    @Override
    public void deleteById(UUID uuid) {
        messages.remove(uuid);
    }

    @Override
    public void save() {
    }
}
