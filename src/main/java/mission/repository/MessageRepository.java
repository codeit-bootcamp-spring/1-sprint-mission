package mission.repository;

import mission.entity.Channel;
import mission.entity.Message;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public interface MessageRepository {
    void createOrUpdateMessage(Message message) throws IOException;
    Set<Message> findAll();
    Message findById(UUID id);
    Set<Message> findMessagesInChannel(Channel channel);

    void delete(UUID messageId);
}
