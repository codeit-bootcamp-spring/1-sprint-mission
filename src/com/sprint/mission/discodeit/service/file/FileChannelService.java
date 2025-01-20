package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.exception.InvalidFormatException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.validation.ChannelValidator;

import java.util.UUID;

public class FileChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ChannelValidator  channelValidator;

    public FileChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
        channelValidator       = ChannelValidator.getInstance();
    }

    /**
     * Create the Channel while ignoring the {@code createAt} and {@code updateAt} fields from {@code channelInfoToCreate}
     */
    @Override
    public Channel createChannel(Channel channelInfoToCreate) throws InvalidFormatException {
        validateFormat(channelInfoToCreate);

        return channelRepository.createChannel(channelInfoToCreate);
    }

    @Override
    public Channel findChannelById(UUID key) {
        return channelRepository.findChannelById(key);
    }

    /**
     * Update the Channel while ignoring the {@code id}, {@code createAt}, {@code updateAt} fields from {@code channelInfoToUpdate}
     */
    @Override
    public Channel updateChannelById(UUID key, Channel channelInfoToUpdate) throws InvalidFormatException {
        validateFormat(channelInfoToUpdate);

        return channelRepository.updateChannelById(key, channelInfoToUpdate);
    }

    private void validateFormat(Channel channelInfoToUpdate)  throws InvalidFormatException {
        channelValidator.validateIdFormat(channelInfoToUpdate);
        channelValidator.validateCreateAtFormat(channelInfoToUpdate);
        channelValidator.validateUpdateAtFormat(channelInfoToUpdate);
        channelValidator.validateNameFormat(channelInfoToUpdate);
    }

    @Override
    public Channel deleteChannelById(UUID key) {
        return channelRepository.deleteChannelById(key);
    }
}
