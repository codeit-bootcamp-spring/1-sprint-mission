package discodeit.service.jcf;

import discodeit.entity.Channel;
import discodeit.service.ChannelService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data; // assume that it is repository

    private JCFChannelService() {
        data = new HashMap<>();
    }

    private static final class InstanceHolder {
        private final static JCFChannelService INSTANCE = new JCFChannelService();
    }

    public static JCFChannelService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Channel create() {
        Channel newChannel = Channel.createChannel();
        UUID userId = newChannel.getCommon().getId();
        return data.putIfAbsent(userId, newChannel);
    }

    @Override
    public Channel read(UUID key) {
        return data.compute(key, (id, user) -> {
            if (user == null) return Channel.createEmptyChannel();

            return user;
        });
    }

    @Override
    public Channel update(UUID key, Channel channelToUpdate) {
        if (data.containsKey(key))
        {
            UUID newKey = channelToUpdate.getCommon().getId();
            data.put(newKey, channelToUpdate);
            data.remove(key);
            return data.get(newKey);
        }

        return Channel.createEmptyChannel();
    }

    @Override
    public Channel delete(UUID key) {
        if (data.containsKey(key))
            return data.remove(key);

        return Channel.createEmptyChannel();
    }
}
