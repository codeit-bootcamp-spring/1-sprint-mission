package discodeit.service.jcf;

import discodeit.Validator.ChannelValidator;
import discodeit.entity.Channel;
import discodeit.entity.Message;
import discodeit.entity.User;
import discodeit.service.ChannelService;
import discodeit.service.MessageService;
import discodeit.service.UserService;

import java.util.*;

public class JCFChannelService implements ChannelService {

    private final ChannelValidator validator;
    private final Map<UUID, Channel> channels;
    private UserService jcfUserService;
    private MessageService jcfMessageService;

    private JCFChannelService() {
        validator = new ChannelValidator();
        channels = new HashMap<>();
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
        validator.validate(name, introduction);
        Channel channel = new Channel(name, introduction, owner);
        channels.put(channel.getId(), channel);
        jcfUserService.updateJoinedChannels(owner, channel);
        return channel;
    }

    @Override
    public Channel find(UUID channelId) {
        Channel channel = channels.get(channelId);

        return Optional.ofNullable(channel)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 채널입니다."));
    }

    @Override
    public List<Channel> findAll() {
        return channels.values().stream().toList();
    }

    @Override
    public String getInfo(Channel channel) {
        return channel.toString();
    }

    @Override
    public void updateName(Channel channel, String name, User user) {
        validator.validateName(name);
        channel.updateName(name, user);
        channel.updateUpdatedAt();
    }

    @Override
    public void updateIntroduction(Channel channel, String introduction, User user) {
        validator.validateIntroduction(introduction);
        channel.updateIntroduction(introduction, user);
        channel.updateUpdatedAt();
    }

    // 유저의 채널 가입은 이 메서드에서만 실행
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
    public void deleteChannel(Channel channel, User user) {
        channel.deleteAllParticipants(user);
        jcfMessageService.deleteAllMessages(channel.getMessages());
        channel.deleteAllMessages();
        channels.remove(channel);
    }

    @Override
    public void deleteParticipant(Channel channel, User user) {
        channel.deleteParticipant(user);
        user.deleteJoinedChannel(channel);
        channel.updateUpdatedAt();
        user.updateUpdatedAt();
    }
}
