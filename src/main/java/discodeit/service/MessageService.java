package discodeit.service;

import discodeit.entity.Channel;
import discodeit.entity.Message;
import discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(String content, User sender, UUID channelId);
    Message find(UUID messageId);
    List<Message> findAll();
    String getInfo(UUID messageId);
    void update(UUID messageId, String content);
    void delete(UUID messageId);
}
