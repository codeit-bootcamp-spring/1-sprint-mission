package discodeit.service.jcf;

import discodeit.Validator.ChannelValidator;
import discodeit.entity.Channel;
import discodeit.entity.User;
import discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {

    private final ChannelValidator validator;
    private final Map<UUID, Channel> channels;

    private JCFChannelService() {
        this.validator = new ChannelValidator();
        this.channels = new HashMap<>();
    }

    private static class JCFChannelServiceHolder {
        private static final ChannelService INSTANCE = new JCFChannelService();
    }

    public static ChannelService getInstance() {
        return JCFChannelServiceHolder.INSTANCE;
    }

    @Override
    public Channel create(String name, String introduction, User owner) {
        validator.validate(name, introduction);
        Channel channel = new Channel(name, introduction, owner);
        channels.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel find(UUID channelId) {
        Channel foundChannel = channels.get(channelId);

        return Optional.ofNullable(foundChannel)
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
    public void update(UUID channelId, String name, String introduction) {
        Channel channel = find(channelId);
        validator.validateName(name);
        validator.validateIntroduction(introduction);

        channel.update(name, introduction);
    }

    @Override
    public void delete(Channel channel) {
        channels.remove(channel.getId());
    }
}
