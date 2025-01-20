package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.UUID;

public class BasicChannelService {
    public static Channel setupChannel(ChannelService channelService, Channel channelInfoToCreate) {
        printStartInfo("setupChannel(ChannelService, Channel)");

        Channel channel = channelService.createChannel(channelInfoToCreate);

        printArgsAndUserInfo(channelInfoToCreate.getId(), channel, "Already exist!");

        return channel;
    }

    public static Channel updateChannel(ChannelService channelService, UUID key, Channel channelInfoToUpdate) {
        printStartInfo("updateChannel(ChannelService, UUID, Channel)");

        Channel channel = channelService.updateChannelById(key, channelInfoToUpdate);

        printArgsAndUserInfo(key, channel, "Not exist!");

        return channel;
    }

    public static Channel searchChannel(ChannelService channelService, UUID key) {
        printStartInfo("searchChannel(ChannelService, UUID)");

        Channel channel = channelService.findChannelById(key);

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
        System.out.println("---------------------------------");
        System.out.println(startInfo);
    }

    private static void printArgsAndUserInfo(UUID key, Channel channel, String messageWhenEmpty) {
        System.out.println("pass UUID '" + key + "'! ");
        if (channel == Channel.EMPTY_CHANNEL) {
            System.out.println(messageWhenEmpty);
        }
        System.out.println("Channel info: " + channel);
        System.out.println();
    }
}
