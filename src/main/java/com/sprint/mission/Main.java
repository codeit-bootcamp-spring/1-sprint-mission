package com.sprint.mission;

import com.sprint.mission.discodit.Service.JCF.JcfChannelService;
import com.sprint.mission.discodit.Service.JCF.JcfMessageService;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        JcfChannelService channelService = new JcfChannelService(new ArrayList<>());
        channelService.add(5);
        channelService.add(10);
        channelService.setList(0,3);
        channelService.remove(0);
        channelService.printList();

        JcfMessageService jcfMessageService = new JcfMessageService(new ArrayList<>());
        channelService.setJcfMessageService(jcfMessageService);
        jcfMessageService.add(5);
        jcfMessageService.add(10);
        channelService.printJcfMessageService();

    }
}