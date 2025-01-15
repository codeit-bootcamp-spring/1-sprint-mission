package com.sprint.mission.discodeit.service.proxy;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.exception.InvalidFormatException;
import com.sprint.mission.discodeit.log.service.ServiceLogger;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.UUID;

public class ChannelServiceProxy implements ChannelService {
    private final ServiceLogger logger;
    private final ChannelService channelService;

    public ChannelServiceProxy(ChannelService channelService) {
        this.channelService = channelService;
        logger = ServiceLogger.getInstance();
    }

    @Override
    public Channel createChannel(Channel channelInfoToCreate) {
        Channel creation   = Channel.createEmptyChannel();
        String  logMessage = "Channel creation failed";
        UUID    channelId  = channelInfoToCreate.getId();

        try {
            creation = channelService.createChannel(channelInfoToCreate);
        } catch (InvalidFormatException e) {
            logger.warning(e.getErrorCode(), logMessage, channelId);
        }

        if (creation == Channel.EMPTY_CHANNEL) {
            logger.warning(logMessage, channelId);
            return creation;
        }

        return creation;
    }

    @Override
    public Channel findChannelById(UUID key) {
        Channel find = channelService.findChannelById(key);
        if (find == Channel.EMPTY_CHANNEL) {
            logger.warning("Channel find failed", key);
            return find;
        }

        return find;
    }

    @Override
    public Channel updateChannelById(UUID key, Channel channelInfoToUpdate) {
        Channel updated   = Channel.createEmptyChannel();
        String logMessage = "Channel update failed";

        try {
            updated = channelService.updateChannelById(key, channelInfoToUpdate);
        } catch (InvalidFormatException e) {
            logger.warning(e.getErrorCode(), logMessage, key);
        }

        if (updated == Channel.EMPTY_CHANNEL) {
            logger.warning(logMessage, key);
            return updated;
        }

        return updated;
    }

    @Override
    public Channel deleteChannelById(UUID key) {
        Channel deletion = channelService.deleteChannelById(key);
        if (deletion == Channel.EMPTY_CHANNEL) {
            logger.warning("Channel deletion failed", key);
            return deletion;
        }

        return deletion;
    }
}
