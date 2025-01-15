package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.exception.InvalidFormatException;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.validation.ChannelValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data; // assume that it is repository
    private final ChannelValidator channelValidator;

    public JCFChannelService() {
        data = new HashMap<>();
        channelValidator = ChannelValidator.getInstance();
    }

    /**
     * Create the Channel while ignoring the {@code createAt} and {@code updateAt} fields from {@code channelInfoToCreate}
     */
    @Override
    public Channel createChannel(Channel channelInfoToCreate) throws InvalidFormatException {
        channelValidator.validateIdFormat(channelInfoToCreate);
        channelValidator.validateCreateAtFormat(channelInfoToCreate);
        channelValidator.validateUpdateAtFormat(channelInfoToCreate);
        channelValidator.validateNameFormat(channelInfoToCreate);

        Channel channelToCreate = Channel.createChannel(
                channelInfoToCreate.getId(),
                channelInfoToCreate.getName()
        );

        return Optional.ofNullable(data.putIfAbsent(channelToCreate.getId(), channelToCreate))
                .map(existingChannel -> Channel.createEmptyChannel())
                .orElse(channelToCreate);
    }

    @Override
    public Channel findChannelById(UUID key) {
        return Optional.ofNullable(data.get(key))
                .orElse(Channel.createEmptyChannel());
    }

    /**
     * Update the Channel while ignoring the {@code id}, {@code createAt}, {@code updateAt} fields from {@code channelInfoToUpdate}
     */
    @Override
    public Channel updateChannelById(UUID key, Channel channelInfoToUpdate) throws InvalidFormatException {
        channelValidator.validateIdFormat(channelInfoToUpdate);
        channelValidator.validateCreateAtFormat(channelInfoToUpdate);
        channelValidator.validateUpdateAtFormat(channelInfoToUpdate);
        channelValidator.validateNameFormat(channelInfoToUpdate);

        Channel existingChannel = findChannelById(key);

        Channel channelToUpdate = Channel.createChannel(
                key,
                existingChannel.getCreateAt(),
                channelInfoToUpdate.getName()
        );

        return Optional.ofNullable(data.computeIfPresent(
                        key, (id, channel) -> channelToUpdate))
                .orElse(Channel.createEmptyChannel());
    }

    @Override
    public Channel deleteChannelById(UUID key) {
        return Optional.ofNullable(data.remove(key))
                .orElse(Channel.createEmptyChannel());
    }
}
