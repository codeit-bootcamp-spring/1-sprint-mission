package mission.controller;

import mission.entity.Message;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface MessageController {

    Message create(UUID channelId, UUID userId, String message);
    Message update(UUID messageId, String newString);
    Message findById(UUID messageId);
    Set<Message> findAll();
    Set<Message> findMessagesByUserId(UUID userId);
    Set<Message> findMessagesInChannel(UUID channelId);
    Set<Message> findContainingMessageInChannel(UUID userId, String writedMessage);
    void delete(UUID messageId);
    void createMessageDirectory();
}
