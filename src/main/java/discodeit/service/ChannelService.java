package discodeit.service;

import discodeit.entity.Channel;
import discodeit.entity.Message;
import discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    void updateUserService(UserService userService);
    void updateMessageService(MessageService messageService);
    Channel createChannel(String name, String introduction, User owner);
    Channel findById(UUID id);
    Channel findChannel(UUID id);
    String getInfo(Channel channel);
    void updateName(Channel channel, String name, User user);
    void updateIntroduction(Channel channel, String introduction, User user);
    void updateParticipants(Channel channel, User user);
    void updateMessages(Channel channel, Message message);
    void deleteChannel(Channel channel, User user);
    void deleteParticipant(Channel channel, User user);
}
