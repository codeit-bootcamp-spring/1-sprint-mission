package com.sprint.mission.discodeit.service.proxy;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.exception.InvalidFormatException;
import com.sprint.mission.discodeit.logger.service.ServiceLogger;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.UUID;

public class ChannelServiceProxy implements ChannelService {
    private final ServiceLogger  logger;
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
            System.err.println(logMessage + ", ID: " + channelId);
        }

        return creation;
    }

    @Override
    public Channel findChannelById(UUID key) {
        return channelService.findChannelById(key);
    }

    @Override
    public Channel updateChannelById(UUID key, Channel channelInfoToUpdate) {
        Channel updated   = Channel.createEmptyChannel();
        String logMessage = "Channel update failed";

        try {
            updated = channelService.updateChannelById(key, channelInfoToUpdate);
        } catch (InvalidFormatException e) {
            logger.warning(e.getErrorCode(), logMessage, key);
            System.err.println(logMessage + ", ID: " + key);
        }

        return updated;
    }

    @Override
    public Channel deleteChannelById(UUID key) {
        return channelService.deleteChannelById(key);
    }
}
