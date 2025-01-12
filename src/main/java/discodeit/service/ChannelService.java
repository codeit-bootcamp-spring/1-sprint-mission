package discodeit.service;

import discodeit.entity.Channel;
import discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    void updateUserService(UserService userService);
    void updateMessageService(MessageService messageService);
    void createChannel(String name, String introduction, User owner);
    UUID getId(Channel channel);
    long getCreatedAt(Channel channel);
    long getUpdatedAt(Channel channel);
    void getInfo(Channel channel);
    String getName(Channel channel);
    String getIntroduction(Channel channel);
    User getOwner(Channel channel);
    List<User> getParticipants(Channel channel);
    void updateName(Channel channel, String name);
    void updateIntroduction(Channel channel, String introduction);
    void updateParticipants(Channel channel, User user);
    void deleteChannel(Channel channel);
    void deleteParticipant(Channel channel, User user);
}
