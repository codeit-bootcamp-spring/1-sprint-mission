package discodeit.service.jcf;

import discodeit.entity.Channel;
import discodeit.entity.User;
import discodeit.service.ChannelService;
import discodeit.service.MessageService;
import discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class JCFChannelService implements ChannelService {

    private UserService jcfUserService;
    private MessageService jcfMessageService;

    private JCFChannelService() {
    }

    private static class JCFChannelServiceHolder {
        private static final ChannelService INSTANCE = new JCFChannelService();
    }

    public static ChannelService getInstance() {
        return JCFChannelServiceHolder.INSTANCE;
    }

    @Override
    public void updateUserService(UserService jcfUserService) {
        this.jcfUserService = jcfUserService;
    }

    @Override
    public void updateMessageService(MessageService jcfMessageService) {
        this.jcfMessageService = jcfMessageService;
    }

    @Override
    public Channel createChannel(String name, String introduction, User owner) {
        return new Channel(name, introduction, owner);
    }

    @Override
    public UUID getId(Channel channel) {
        return channel.getId();
    }

    @Override
    public long getCreatedAt(Channel channel) {
        return channel.getCreatedAt();
    }

    @Override
    public long getUpdatedAt(Channel channel) {
        return channel.getUpdatedAt();
    }

    @Override
    public String getInfo(Channel channel) {
        return channel.toString();
    }

    @Override
    public String getName(Channel channel) {
        return channel.getName();
    }

    @Override
    public String getIntroduction(Channel channel) {
        return channel.getIntroduction();
    }

    @Override
    public User getOwner(Channel channel) {
        return channel.getOwner();
    }

    @Override
    public List<User> getParticipants(Channel channel) {
        return channel.getParticipants();
    }

    @Override
    public void updateName(Channel channel, String name) {
        channel.updateName(name);
    }

    @Override
    public void updateIntroduction(Channel channel, String introduction) {
        channel.updateIntroduction(introduction);
    }

    @Override
    public void updateParticipants(Channel channel, User user) {
        channel.updateParticipants(user);
    }

    @Override
    public void deleteChannel(Channel channel) {

    }

    @Override
    public void deleteParticipant(Channel channel, User user) {
        channel.deleteParticipant(user);
    }
}
