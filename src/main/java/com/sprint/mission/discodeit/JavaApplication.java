package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;

import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        // User 서비스 테스트
        JCFUserService userService = new JCFUserService();
        User user1 = new User("JohnDoe");
        userService.createUser(user1);
        userService.getUser(user1.getId());
        userService.getAllUsers();
        user1.setUsername("JohnUpdated");
        userService.updateUser(user1);
        userService.getUser(user1.getId());
        userService.deleteUser(user1.getId());
        userService.getAllUsers();

        // Channel 서비스 테스트
        JCFChannelService channelService = new JCFChannelService();
        Channel channel1 = new Channel("General");
        channelService.createChannel(channel1);
        channelService.getChannel(channel1.getId());
        channelService.getAllChannels();
        channel1.setName("GeneralUpdated");
        channelService.updateChannel(channel1);
        channelService.getChannel(channel1.getId());
        channelService.deleteChannel(channel1.getId());
        channelService.getAllChannels();

        // Message 서비스 테스트
        JCFMessageService messageService = new JCFMessageService();
        Message message1 = new Message("Hello, World!", user1.getId(), channel1.getId());
        messageService.createMessage(message1);
        messageService.getMessage(message1.getId());
        messageService.getAllMessages();
        message1.setContent("Hello, Updated World!");
        messageService.updateMessage(message1);
        messageService.getMessage(message1.getId());
        messageService.deleteMessage(message1.getId());
        messageService.getAllMessages();
    }
}
