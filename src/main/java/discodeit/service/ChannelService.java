package discodeit.service;

import discodeit.entity.Channel;
import discodeit.entity.User;

import java.util.List;

public interface ChannelService {
    Channel create(Channel newChannel);
    Channel readById(String channelId);
    List<Channel> readAll();
    Channel update(String channelId, Channel updateChannel);
    void deleteChannel(String channelId);
    void addUser(String channelId, User user);
    List<User> getUserList(String channelId);
    void deleteUser(String channelId, User user);
}
