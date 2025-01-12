package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.validation.ChannelValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data; // assume that it is repository
    private final ChannelValidator   channelValidator;

    private JCFChannelService() {
        data             = new HashMap<>();
        channelValidator = ChannelValidator.getInstance();
    }

    private static final class InstanceHolder {
        private final static JCFChannelService INSTANCE = new JCFChannelService();
    }

    public static JCFChannelService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Channel createChannel(Channel channelToCreate) {
        channelValidator.validateBaseEntityFormat(channelToCreate);
        channelValidator.validateNameFormat(channelToCreate);

        UUID key = channelToCreate.getId();
        return Optional.ofNullable(data.putIfAbsent(key, channelToCreate))
                .map(existingChannel -> Channel.createEmptyChannel())
                .orElse(channelToCreate);
    }

    @Override
    public Channel findChannelById(UUID key) {
        return Optional.ofNullable(data.get(key))
                .orElse(Channel.createEmptyChannel());
    }

    @Override
    public Channel updateChannelById(UUID key, Channel channelToUpdate) {
        Channel existingChannel = findChannelById(key);
        channelValidator.validateChannelIdEquality(existingChannel, channelToUpdate);
        channelValidator.validateBaseEntityFormat(channelToUpdate);
        channelValidator.validateNameFormat(channelToUpdate);

        return Optional.ofNullable(data.computeIfPresent(
                        key, (id, channel)-> channelToUpdate))
                .orElse(Channel.createEmptyChannel());
    }

    @Override
    public Channel deleteChannelById(UUID key) {
        return Optional.ofNullable(data.remove(key))
                .orElse(Channel.createEmptyChannel());
    }
}
