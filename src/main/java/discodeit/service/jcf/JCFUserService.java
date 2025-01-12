package discodeit.service.jcf;

import discodeit.entity.Channel;
import discodeit.entity.User;
import discodeit.service.ChannelService;
import discodeit.service.MessageService;
import discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {

    private ChannelService jcfChannelService;
    private MessageService jcfMessageService;

    private JCFUserService() {
    }

    private static class JCFUserServiceHolder {
        private static final UserService INSTANCE = new JCFUserService();
    }

    public static UserService getInstance() {
        return JCFUserServiceHolder.INSTANCE;
    }

    @Override
    public void updateChannelService(ChannelService jcfChannelService) {
        this.jcfChannelService = jcfChannelService;
    }

    @Override
    public void updateMessageService(MessageService jcfMessageService) {
        this.jcfMessageService = jcfMessageService;
    }

    @Override
    public User createUser(String name, String email, String phoneNumber, String password) {
        return new User(name, email, phoneNumber, password);
    }

    @Override
    public UUID readId(User user) {
        return user.getId();
    }

    @Override
    public long getCreatedAt(User user) {
        return user.getCreatedAt();
    }

    @Override
    public long getUpdatedAt(User user) {
        return user.getUpdatedAt();
    }

    @Override
    public String getInfo(User user) {
        return user.toString();
    }

    @Override
    public String getName(User user) {
        return user.getName();
    }

    @Override
    public String getEmail(User user) {
        return user.getEmail();
    }

    @Override
    public String getPhoneNumber(User user) {
        return user.getPhoneNumber();
    }

    @Override
    public List<Channel> getJoinedChannels(User user) {
        return user.getJoinedChannels();
    }

    @Override
    public void updateName(User user, String name) {
        user.updateName(name);
        user.updateUpdatedAt();
    }

    @Override
    public void updateEmail(User user, String email) {
        user.updateEmail(email);
        user.updateUpdatedAt();
    }

    @Override
    public void updatePhoneNumber(User user, String phoneNumber) {
        user.updatePhoneNumber(phoneNumber);
        user.updateUpdatedAt();
    }

    @Override
    public void updatePassword(User user, String originalPassword, String newPassword) {
        user.updatePassword(originalPassword, newPassword);
        user.updateUpdatedAt();
    }

    @Override
    public void updateJoinedChannels(User user, Channel channel) {
        user.updateJoinedChannels(channel);
        user.updateUpdatedAt();
    }

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public void deleteJoinedChannel(User user, Channel channel) {
        user.deleteJoinedChannel(channel);
        user.updateUpdatedAt();
    }
}
