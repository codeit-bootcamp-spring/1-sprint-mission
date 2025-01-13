package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;
import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        // 서비스 객체 생성
        JCFUserService userService = new JCFUserService();
        JCFChannelService channelService = new JCFChannelService();
        JCFMessageService messageService = new JCFMessageService();

        // 사용자 생성
        User user1 = userService.createUser(UUID.randomUUID(), "Alice");
        User user2 = userService.createUser(UUID.randomUUID(), "Bob");

        // 채널 생성
        Channel channel1 = channelService.createChannel(UUID.randomUUID(), "Alice", "ch1");
        Channel channel2 = channelService.createChannel(UUID.randomUUID(), "Bob", "ch2");

        // 메시지 생성
        Message message1 = messageService.createMessage(UUID.randomUUID(), "Alice", "Hello, world!");
        Message message2 = messageService.createMessage(UUID.randomUUID(), "Bob", "Hi Alice!");

        // 출력
        System.out.println("사용자 UUID:");
        System.out.println(user1.getId());
        System.out.println(user2.getId());
        System.out.println("============================");

        System.out.println("채널 정보:");
        System.out.println(channel1.getChannelName());
        System.out.println(channel2.getChannelName());
        System.out.println("============================");

        System.out.println("메시지 정보:");
        System.out.println(message1.getMessage());
        System.out.println(message2.getMessage());
        System.out.println("============================");

        // 사용자 리스트 출력
        System.out.println("모든 사용자 출력:");
        List<User> users = userService.allUsers();
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            System.out.println("User " + (i + 1));
            System.out.println("userId: " + user.getId());
            System.out.println("userName: " + user.getUserName());
            System.out.println("createAt: " + user.getCreateAt());
            System.out.println("------------------------");
        }

        // 채널 리스트 출력
        System.out.println("채널 리스트:");
        List<Channel> channels = channelService.allChannel();
        for (int i = 0; i < channels.size(); i++) {
            Channel channel = channels.get(i);
            System.out.println("Channel " + (i + 1));
            System.out.println("channelId: " + channel.getId());
            System.out.println("channelName: " + channel.getChannelName());
            System.out.println("------------------------");
        }

        // 메시지 리스트 출력
        System.out.println("메시지 출력:");
        List<Message> messages = messageService.getAllMessages();
        for (int i = 0; i < messages.size(); i++) {
            Message message = messages.get(i);
            System.out.println("Message " + (i + 1));
            System.out.println("messageId: " + message.getId());
            System.out.println("messageContent: " + message.getMessage());
            System.out.println("------------------------");
        }

        // 사용자 삭제
        System.out.println("사용자 삭제");
        userService.deleteUser(user1.getId());

        userService.printAllUsers();

        // 채널 삭제
        System.out.println("채널 삭제");
        channelService.deleteChannel(channel2.getId());

        List<Channel> updatedChannels = channelService.allChannel();
        for (int i = 0; i < updatedChannels.size(); i++) {
            Channel updatedChannel = updatedChannels.get(i);
            System.out.println("Channel " + (i + 1));
            System.out.println("channelId: " + updatedChannel.getId());
            System.out.println("channelName: " + updatedChannel.getChannelName());
            System.out.println("------------------------");
        }

        // 메시지 삭제
        System.out.println("메시지 삭제");
        messageService.deleteMessage(message1.getId());

        // 삭제 후 메시지 리스트 출력
        System.out.println("모든 메시지 출력:");
        List<Message> updatedMessages = messageService.getAllMessages();
        for (int i = 0; i < updatedMessages.size(); i++) {
            Message updatedMessage = updatedMessages.get(i);
            System.out.println("Message " + (i + 1));
            System.out.println("messageId: " + updatedMessage.getId());
            System.out.println("messageContent: " + updatedMessage.getMessage());
            System.out.println("------------------------");
        }
    }
}
