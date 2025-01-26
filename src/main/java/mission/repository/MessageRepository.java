package mission.repository;

import mission.entity.Channel;
import mission.entity.Message;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public interface MessageRepository {
    Message createOrUpdateMessage(Message message) throws IOException;
    Message findById(UUID id);
    Set<Message> findAll();
    Set<Message> findMessagesInChannel(Channel channel);

    void delete(UUID messageId);
}
