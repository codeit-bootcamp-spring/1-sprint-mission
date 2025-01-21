package discodeit.service;

import discodeit.entity.Channel;
import discodeit.entity.User;

import java.util.UUID;

public interface UserService {
    void updateChannelService(ChannelService channelService);
    void updateMessageService(MessageService messageService);
    User createUser(String name, String email, String phoneNumber, String password);
    User findById(UUID id);
    User findUser(UUID id);
    String getInfo(User user);
    void updateName(User user, String name);
    void updateEmail(User user, String email);
    void updatePhoneNumber(User user, String phoneNumber);
    void updatePassword(User user, String originalPassword, String newPassword);
    void updateJoinedChannels(User user, Channel channel);
    void deleteUser(User user);
    void deleteJoinedChannel(User user, Channel channel);
    void isDuplicateEmail(String email);
    void isDuplicatePhoneNumber(String phoneNumber);
}
