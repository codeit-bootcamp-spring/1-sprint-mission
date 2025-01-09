package com.sprint.mission;

import com.sprint.mission.discodit.Entity.Channel;
import com.sprint.mission.discodit.Entity.Message;
import com.sprint.mission.discodit.Entity.User;
import com.sprint.mission.discodit.Service.JCF.JcfChannelService;
import com.sprint.mission.discodit.Service.JCF.JcfMessageService;

import java.util.ArrayList;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        User user1 = new User(UUID.randomUUID());
        User user2 = new User(UUID.randomUUID());

        Channel channel1 = new Channel(UUID.randomUUID());
        Channel channel2 = new Channel(UUID.randomUUID());
        Channel channel3 = new Channel(UUID.randomUUID());

        Message message1 = new Message(UUID.randomUUID());
        Message message2 = new Message(UUID.randomUUID());

        JcfChannelService channelService = new JcfChannelService(new ArrayList<>());
        JcfMessageService jcfMessageService = new JcfMessageService(new ArrayList<>());

        channelService.add(channel1);
        channelService.add(channel2);
        channelService.setList(0, channel3);
        channelService.remove(0);
        channelService.printList();

        channelService.setJcfMessageService(jcfMessageService);
        jcfMessageService.add(message1);
        jcfMessageService.add(message2);
        channelService.printJcfMessageService();

    }
}