package mission.service;


import mission.entity.Channel;
import mission.entity.Message;
import mission.entity.User;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface MessageService {
    Message createOrUpdate(Message message) throws IOException;
    Message update(UUID messageId, String newMassage);
    Message findById(UUID messageId);
    Set<Message> findAll();
    Set<Message> findMessagesInChannel(Channel channel);
    void delete(UUID messageId);
}
