package discodeit.repository;

import discodeit.entity.Message;
import discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageRepository {
    Message save(String content, User sender, UUID channelId);
    Message find(UUID messageId);
    List<Message> findAll();
    void update(UUID messageId, String content);
    void delete(UUID messageId);
}
