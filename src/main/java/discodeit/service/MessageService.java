package discodeit.service;

import discodeit.entity.Message;
import discodeit.entity.User;

import java.util.UUID;

public interface MessageService {
    void updateUserService(UserService userService);
    void updateChannelService(ChannelService channelService);
    void createMessage(String content, User sender);
    UUID readId(Message message);
    long getCreateAt(Message message);
    long getUpdatedAt(Message message);
    void getInfo(Message message);
    String getContent(Message message);
    User getSender(Message message);
    void updateContent(Message message);
    void deleteMessage(Message message);
}
