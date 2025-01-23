package discodeit.service;

import discodeit.entity.Channel;
import discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    void updateChannelService(ChannelService channelService);
    User createUser(String name, String email, String phoneNumber, String password);
    User find(UUID userId);
    List<User> findAll();
    String getInfo(User user);
    void update(UUID userId, String name, String email, String phoneNumber);
    void updatePassword(User user, String originalPassword, String newPassword);
    void updateJoinedChannels(User user, Channel channel);
    void deleteUser(User user);
    void deleteJoinedChannel(User user, Channel channel);
    void isDuplicateEmail(String email);
    void isDuplicatePhoneNumber(String phoneNumber);
}
