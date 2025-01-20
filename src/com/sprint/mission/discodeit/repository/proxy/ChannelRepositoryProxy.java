package com.sprint.mission.discodeit.repository.proxy;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.log.RepositoryLogger;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.UUID;

public class ChannelRepositoryProxy implements ChannelRepository {
    private final RepositoryLogger  logger;
    private final ChannelRepository channelRepository;

    public ChannelRepositoryProxy(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
        logger = RepositoryLogger.getInstance();
    }

    @Override
    public Channel createChannel(Channel channelInfoToCreate) {
        Channel creation   = channelRepository.createChannel(channelInfoToCreate);
        String  logMessage = "Channel creation failed";
        UUID    channelId  = channelInfoToCreate.getId();

        if (creation == Channel.EMPTY_CHANNEL) {
            logger.warning(logMessage, channelId);
        }

        return creation;
    }

    @Override
    public Channel findChannelById(UUID key) {
        Channel find = channelRepository.findChannelById(key);

        if (find == Channel.EMPTY_CHANNEL) {
            logger.warning("Channel find failed", key);
        }

        return find;
    }

    @Override
    public Channel updateChannelById(UUID key, Channel channelInfoToUpdate) {
        Channel updated   = channelRepository.updateChannelById(key, channelInfoToUpdate);
        String logMessage = "Channel update failed";

        if (updated == Channel.EMPTY_CHANNEL) {
            logger.warning(logMessage, key);
        }

        return updated;
    }

    @Override
    public Channel deleteChannelById(UUID key) {
        Channel deletion = channelRepository.deleteChannelById(key);

        if (deletion == Channel.EMPTY_CHANNEL) {
            logger.warning("Channel deletion failed", key);
        }

        return deletion;
    }
}
