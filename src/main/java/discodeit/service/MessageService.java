package discodeit.service;

import discodeit.entity.Message;
import discodeit.entity.User;

import java.util.UUID;

public interface MessageService {
    void updateUserService(UserService userService);
    void updateChannelService(ChannelService channelService);
    Message createMessage(String content, User sender);
    UUID readId(Message message);
    long getCreateAt(Message message);
    long getUpdatedAt(Message message);
    String getInfo(Message message);
    String getContent(Message message);
    User getSender(Message message);
    void updateContent(Message message, String content);
    void deleteMessage(Message message);
}
