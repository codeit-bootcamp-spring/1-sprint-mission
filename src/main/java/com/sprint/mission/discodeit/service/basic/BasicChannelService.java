package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.logger.service.ServiceLogger;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.UUID;

public class BasicChannelService {

    private static final ServiceLogger logger = ServiceLogger.getInstance();


    public static Channel setupChannel(ChannelService channelService, Channel channelInfoToCreate) {
        printStartInfo("setupChannel(ChannelService, Channel)");

        Channel channel = channelService.registerChannel(channelInfoToCreate);

        printArgsAndUserInfo(channelInfoToCreate.getId(), channel, "Already exist!");

        return channel;
    }

    public static Channel updateChannel(ChannelService channelService, UUID key,
        Channel channelInfoToUpdate) {
        printStartInfo("updateChannel(ChannelService, UUID, Channel)");

        Channel channel = channelService.updateChannelById(key, channelInfoToUpdate);

        printArgsAndUserInfo(key, channel, "Not exist!");

        return channel;
    }

    public static Channel searchChannel(ChannelService channelService, UUID key) {
        printStartInfo("searchChannel(ChannelService, UUID)");

        Channel channel = channelService.searchChannelById(key);

        printArgsAndUserInfo(key, channel, "Not exist!");

        return channel;
    }

    public static Channel removeChannel(ChannelService channelService, UUID key) {
        printStartInfo("removeChannel(ChannelService, UUID)");

        Channel channel = channelService.deleteChannelById(key);

        printArgsAndUserInfo(key, channel, "Not exist!");

        return channel;
    }

    private static void printStartInfo(String startInfo) {
        logger.info("---------------------------------");
        logger.info(startInfo);
    }

    private static void printArgsAndUserInfo(UUID key, Channel channel, String messageWhenEmpty) {
        logger.info("pass UUID '" + key + "'! ");
        if (channel == Channel.EMPTY_CHANNEL) {
            logger.info(messageWhenEmpty);
        }
        logger.info("Channel info: " + channel);
    }
}
