package discodeit.service;

import discodeit.entity.Channel;
import discodeit.entity.Message;
import discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    void updateUserService(UserService userService);
    void updateChannelService(ChannelService channelService);
    Message createMessage(Channel channel, String content, User sender);
    Message find(UUID messageId);
    List<Message> findAll();
    String getInfo(Message message);
    void updateContent(Message message, String content);
    void deleteMessage(Message message, Channel channel, User user);
    void deleteAllMessages(List<Message> deleteMessages);
}
