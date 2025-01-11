package discodeit.service.jcf;

import discodeit.entity.Channel;
import discodeit.entity.User;
import discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {

    @Override
    public void createUser(String name, String email, String phoneNumber, String password) {

    }

    @Override
    public UUID readId(User user) {
        return null;
    }

    @Override
    public long getCreatedAt(User user) {
        return 0;
    }

    @Override
    public long getUpdatedAt(User user) {
        return 0;
    }

    @Override
    public void getInfo(User user) {

    }

    @Override
    public String getName(User user) {
        return "";
    }

    @Override
    public String getEmail(User user) {
        return "";
    }

    @Override
    public String getPhoneNumber(User user) {
        return "";
    }

    @Override
    public List<Channel> getJoinedChannels(User user) {
        return List.of();
    }

    @Override
    public void updateName(User user, String name) {

    }

    @Override
    public void updateEmail(User user, String email) {

    }

    @Override
    public void updatePhoneNumber(User user, String phoneNumber) {

    }

    @Override
    public void updatePassword(User user, String password) {

    }

    @Override
    public void updateJoinedChannels(User user, Channel channel) {

    }

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public void deleteJoinedChannel(User user, Channel channel) {

    }
}
