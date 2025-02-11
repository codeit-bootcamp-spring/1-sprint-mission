package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;


public class DiscodeitApplication {
    public static void main(String[] args) {

        UserService fileUserService = new FileUserService();
        ChannelService fileChannelService = new FileChannelService();
        MessageService fileMessageService = new FileMessageService();

        UserService jcfUserService = new JCFUserService();
        ChannelService jcfChannelService = new JCFChannelService();
        MessageService jcfMessageService = new JCFMessageService();

        User user = new User("성근", "leesin014@naver.com", "pad13");
        jcfUserService.registerUser(user);
        fileUserService.registerUser(user);

        Channel channel = new Channel( ChannelType.PUBLIC, "codeit", user);
        jcfChannelService.createChannel(channel);
        fileChannelService.createChannel(channel);
        channel.addMember(user);

        Message message = new Message(channel, user, "반갑습니다");
        jcfMessageService.createMessage(message);
        fileMessageService.createMessage(message);

        System.out.println("-------JCF-------");
        jcfUserService.getAllUser();
        System.out.println(jcfChannelService.getAllChannel());
        System.out.println(jcfMessageService.getAllMessage());

        System.out.println("-------File-------");
        fileUserService.getAllUser();
        System.out.println(fileChannelService.getAllChannel());
        System.out.println(fileMessageService.getAllMessage());
    }
}
