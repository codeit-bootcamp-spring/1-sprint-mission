package discodeit.service;

import discodeit.entity.Channel;
import discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(String name, String email, String phoneNumber, String password);
    UUID readId(User user);
    long getCreatedAt(User user);
    long getUpdatedAt(User user);
    String getInfo(User user);
    String getName(User user);
    String getEmail(User user);
    String getPhoneNumber(User user);
    List<Channel> getJoinedChannels(User user);
    void updateName(User user, String name);
    void updateEmail(User user, String email);
    void updatePhoneNumber(User user, String phoneNumber);
    void updatePassword(User user, String originalPassword, String newPassword);
    void updateJoinedChannels(User user, Channel channel);
    void deleteUser(User user);
    void deleteJoinedChannel(User user, Channel channel);
}
