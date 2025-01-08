package sprint.mission.discodeit.service.jcf;

import sprint.mission.discodeit.entity.Channel;
import sprint.mission.discodeit.service.ChannelService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
    public Channel create(Channel channelToCreate) {
        UUID key = channelToCreate.getCommon().getId();
        return Optional.ofNullable(data.putIfAbsent(key, channelToCreate))
                .map(existingChannel -> Channel.createEmptyChannel())
                .orElse(channelToCreate);
    }

    @Override
    public Channel read(UUID key) {
        return Optional.ofNullable(data.get(key))
                .orElse(Channel.createEmptyChannel());
    }

    @Override
    public Channel update(UUID key, Channel channelToUpdate) {
        return Optional.ofNullable(data.computeIfPresent(
                        key, (id, user)-> channelToUpdate))
                .orElse(Channel.createEmptyChannel());
    }

    @Override
    public Channel delete(UUID key) {
        return Optional.ofNullable(data.remove(key))
                .orElse(Channel.createEmptyChannel());
    }
}
