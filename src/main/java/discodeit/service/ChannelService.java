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
    Channel find(UUID channelId);
    List<Channel> findAll();
    String getInfo(Channel channel);
    void update(UUID channelId, String name, String introduction);
    void deleteChannel(Channel channel);
}
