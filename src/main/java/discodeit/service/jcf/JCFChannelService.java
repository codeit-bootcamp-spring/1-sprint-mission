package discodeit.service.jcf;

import discodeit.entity.Channel;
import discodeit.entity.Message;
import discodeit.entity.User;
import discodeit.service.ChannelService;
import discodeit.service.MessageService;
import discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFChannelService implements ChannelService {

    private UserService jcfUserService;
    private MessageService jcfMessageService;
    private final List<Channel> channels;

    private JCFChannelService() {
        channels = new ArrayList<>();
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
        Channel newChannel = new Channel(name, introduction, owner);
        channels.add(newChannel);
        jcfUserService.updateJoinedChannels(owner, newChannel);
        return newChannel;
    }

    @Override
    public Channel findById(UUID id) {
        Channel findChannel = findChannel(id);
        if (findChannel == null) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        }
        return findChannel;
    }

    @Override
    public Channel findChannel(UUID id) {
        return channels.stream()
                .filter(channel -> channel.isIdEqualTo(id))
                .findAny()
                .orElse(null);
    }

    @Override
    public String getInfo(Channel channel) {
        return channel.toString();
    }

    @Override
    public void updateName(Channel channel, String name) {
        channel.updateName(name);
        channel.updateUpdatedAt();
    }

    @Override
    public void updateIntroduction(Channel channel, String introduction) {
        channel.updateIntroduction(introduction);
        channel.updateUpdatedAt();
    }

    @Override
    public void updateParticipants(Channel channel, User user) {
        channel.updateParticipants(user);
        jcfUserService.updateJoinedChannels(user, channel);
        channel.updateUpdatedAt();
    }

    @Override
    public void updateMessages(Channel channel, Message message) {
        channel.updateMessages(message);
    }

    @Override
    public void deleteChannel(Channel channel) {
        channel.deleteAllParticipants();
        Channel finalChannel = channel;
        channel.getMessages().stream().forEach(message -> jcfMessageService.deleteMessage(finalChannel, message));
        channels.remove(channel);
        channel = null;
    }

    @Override
    public void deleteParticipant(Channel channel, User user) {
        channel.deleteParticipant(user);
        user.deleteJoinedChannel(channel);
        channel.updateUpdatedAt();
        user.updateUpdatedAt();
    }
}
