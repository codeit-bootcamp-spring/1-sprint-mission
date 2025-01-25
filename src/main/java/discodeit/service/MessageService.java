package discodeit.service;

import discodeit.entity.Channel;
import discodeit.entity.Message;
import discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    void updateUserService(UserService userService);
    void updateChannelService(ChannelService channelService);
    Message create(String content, User sender, UUID channelId);
    Message find(UUID messageId);
    List<Message> findAll();
    String getInfo(Message message);
    void update(UUID messageId, String content);
    void delete(UUID messageId);
}
