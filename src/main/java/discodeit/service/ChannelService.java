package discodeit.service;

import discodeit.entity.Channel;
import discodeit.entity.User;

import java.util.UUID;

public interface ChannelService {
    void updateUserService(UserService userService);
    void updateMessageService(MessageService messageService);
    Channel createChannel(String name, String introduction, User owner);
    Channel findById(UUID id);
    Channel findChannel(UUID id);
    String getInfo(Channel channel);
    void updateName(Channel channel, String name);
    void updateIntroduction(Channel channel, String introduction);
    void updateParticipants(Channel channel, User user);
    void deleteChannel(Channel channel);
    void deleteParticipant(Channel channel, User user);
}
