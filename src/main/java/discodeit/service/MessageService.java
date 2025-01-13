package discodeit.service;

import discodeit.entity.Channel;
import discodeit.entity.Message;
import discodeit.entity.User;

import java.util.UUID;

public interface MessageService {
    void updateUserService(UserService userService);
    void updateChannelService(ChannelService channelService);
    Message createMessage(String content, User sender);
    Message findById(UUID id);
    Message findMessage(UUID id);
    String getInfo(Message message);
    void updateContent(Message message, String content);
    void deleteMessage(Message message);
}
