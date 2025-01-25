package com.sprint.mission.discodeit.app;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.jcf.JCFChannelService;
import com.sprint.mission.discodeit.jcf.JCFMessageService;
import com.sprint.mission.discodeit.jcf.JCFUserService;

public class JavaApplication {
    public static void main(String[] args) {
        //각 서비스의 인스턴스 생성, 의존성 연결
        JCFUserService userService = JCFUserService.getInstance();
        JCFChannelService channelService = JCFChannelService.getInstance(userService);
        JCFMessageService messageService = JCFMessageService.getInstance(userService, channelService);


        // 사용자 생성
        User user1 = new User("Alice", "alice123");
        User user2 = new User("Bob", "bob456");

        userService.createUser(user1);
        userService.createUser(user2);

        // 채널 생성
        Channel channel1 = new Channel("Tech Talks", "alice123");
        Channel channel2 = new Channel("Cooking Show", "bob456");

        channelService.createChannel(channel1);
        channelService.createChannel(channel2);

        // 메시지 생성
        Message message1 = new Message("alice123", "Hello, welcome to Tech Talks!");
        Message message2 = new Message("bob456", "Today we will make pasta.");

        messageService.createMessage(message1);
        messageService.createMessage(message2);

        // 데이터 출력
        System.out.println("\nAll Users:");
        userService.readAllUsers().forEach(user ->
                System.out.println("User: " + user.getUserName() + ", ID: " + user.getUserId()));

        System.out.println("\nAll Channels:");
        channelService.readAllChannels().forEach(channel ->
                System.out.println("Channel: " + channel.getChannelTitle() + ", User ID: " + channel.getUserId()));

        System.out.println("\nAll Messages:");
        messageService.readAllMessages().forEach(message ->
                System.out.println("Message: " + message.getMessageText() + ", User ID: " + message.getUserId()));

        // 특정 사용자 메시지 업데이트
        messageService.updateMessage(message1.getMessageUuid().toString(), "Updated: Welcome to our Tech Talk series!");


        // 메시지 출력
        System.out.println("\nUpdated Messages:");
        messageService.readAllMessages().forEach(message ->
                System.out.println("Message: " + message.getMessageText() + ", User ID: " + message.getUserId()));



    }
}
