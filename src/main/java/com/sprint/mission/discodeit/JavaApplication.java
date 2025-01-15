package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;
import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        // UserService
        JCFUserService userService = new JCFUserService();
        JCFChannelService channelService = new JCFChannelService();
        JCFMessageService messageService = new JCFMessageService(userService, channelService);

        // User 등록
        System.out.println("=== 유저 등록 ===");
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();
        UUID userId3 = UUID.randomUUID();

        User user1 = new User(userId1, System.currentTimeMillis(), System.currentTimeMillis(), "Alice");
        User user2 = new User(userId2, System.currentTimeMillis(), System.currentTimeMillis(), "Bob");
        User user3 = new User(userId3, System.currentTimeMillis(), System.currentTimeMillis(), "Charlie");

        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);

        System.out.println(user1.getUserName() + " 유저가 등록되었습니다.");
        System.out.println(user2.getUserName() + " 유저가 등록되었습니다.");
        System.out.println(user3.getUserName() + " 유저가 등록되었습니다.");

        System.out.println();


        // Channel 등록
        System.out.println("=== 채널 등록 ===");
        UUID channelId1 = UUID.randomUUID();
        UUID channelId2 = UUID.randomUUID();
        UUID channelId3 = UUID.randomUUID();

        Channel channel1 = new Channel(channelId1, System.currentTimeMillis(), System.currentTimeMillis(), "채널1");
        Channel channel2 = new Channel(channelId2, System.currentTimeMillis(), System.currentTimeMillis(), "채널2");
        Channel channel3 = new Channel(channelId3, System.currentTimeMillis(), System.currentTimeMillis(), "채널3");

        channelService.createChannel(channel1);
        channelService.createChannel(channel2);
        channelService.createChannel(channel3);

        System.out.println(channel1.getChannelName() + " 채널이 등록되었습니다.");
        System.out.println(channel2.getChannelName() + " 채널이 등록되었습니다.");
        System.out.println(channel3.getChannelName() + " 채널이 등록되었습니다.");

        System.out.println();

        // Message 등록
        System.out.println("=== 메시지 등록 ===");
        UUID messageId1 = UUID.randomUUID();
        UUID messageId2 = UUID.randomUUID();
        UUID messageId3 = UUID.randomUUID();

        Message message1 = new Message(messageId1, System.currentTimeMillis(), System.currentTimeMillis(), "Hello", user1, channel1);
        Message message2 = new Message(messageId2, System.currentTimeMillis(), System.currentTimeMillis(), "Hi", user2, channel1);
        Message message3 = new Message(messageId3, System.currentTimeMillis(), System.currentTimeMillis(), "안녕", user1, channel1);

        try {
            messageService.createMessage(message1);
            System.out.println(message1.getContent() + " 메시지가 등록되었습니다.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        try {
            messageService.createMessage(message2);
            System.out.println(message2.getContent() + " 메시지가 등록되었습니다.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        try {
            messageService.createMessage(message3);
            System.out.println(message3.getContent() + " 메시지가 등록되었습니다.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        System.out.println();

        // User 단건 조회
        System.out.println("=== 유저 단건 조회 ===");
        System.out.println("유저1: " + userService.getUser(userId1).getUserName());

        // Channel 단건 조회
        System.out.println("=== 채널 단건 조회 ===");
        System.out.println("채널1: " + channelService.getChannel(channelId1).getChannelName());

        // Message 단건 조회
        System.out.println("=== 메시지 단건 조회 ===");
        System.out.println("메시지1: " + messageService.getMessage(messageId1).getContent());

        System.out.println();

        // User 다건 조회
        System.out.println("=== 유저 다건 조회 ===");
        List<User> allUsers = userService.getAllUsers();
        for (User user : allUsers) {
            System.out.println("유저: " + user.getUserName());
        }

        System.out.println();

        // Channel 다건 조회
        System.out.println("=== 채널 다건 조회 ===");
        List<Channel> allChannels = channelService.getAllChannels();
        for (Channel channel : allChannels) {
            System.out.println("채널: " + channel.getChannelName());
        }

        System.out.println();
        
        // Message 다건 조회
        System.out.println("=== 메시지 다건 조회 ===");
        List<Message> allMessages = messageService.getAllMessages();
        for(Message message : allMessages) {
            System.out.println("메시지: " + message.getContent());
        }

        System.out.println();

        // User 수정
        userService.updateUser(userId1, "Lia");
        System.out.println("=== 유저1 수정 ===");

        // Channel 수정
        channelService.updateChannel(channelId1, "채널11");
        System.out.println("=== 채널1 수정 ===");

        // Message 수정
        messageService.updateMessage(messageId1, "Bye");
        System.out.println("=== 메시지1 수정 ===");

        System.out.println();

        // User 수정 출력
        System.out.println("=== 수정된 유저 ===");
        System.out.println("유저: " + userService.getUser(userId1).getUserName());
        
        // Channel 수정 출력
        System.out.println("=== 수정된 채널 ===");
        System.out.println("채널: " + channelService.getChannel(channelId1).getChannelName());

        // Message 수정 출력
        System.out.println("=== 수정된 메시지 ===");
        System.out.println("메시지: " + messageService.getMessage(messageId1).getContent());

        System.out.println();

        // User 삭제
        userService.deleteUser(userId1);
        System.out.println("=== 유저1 삭제 ===");

        // Channel 삭제
        channelService.deleteChannel(channelId1);
        System.out.println("=== 채널1 삭제 ===");

        // Message 삭제
        messageService.deleteMessage(messageId1);
        System.out.println("=== 메시지1 삭제 ===");

        System.out.println();

        // 삭제 조회
        System.out.println("=== 삭제 후 조회 ===");

        User deletedUser = userService.getUser(userId1);
        if (deletedUser == null) {
            System.out.println("유저가 존재하지 않습니다.");  // 유저가 없을 경우
        } else {
            System.out.println("유저: " + deletedUser.getUserName());
        }

        Channel deletedChannel = channelService.getChannel(channelId1);
        if (deletedChannel == null) {
            System.out.println("채널이 존재하지 않습니다.");  // 채널이 없을 경우
        } else {
            System.out.println("채널: " + deletedChannel.getChannelName());
        }

        Message deletedMessage = messageService.getMessage(messageId1);
        if (deletedMessage == null) {
            System.out.println("메시지가 존재하지 않습니다.");  // 메시지가 없을 경우
        } else {
            System.out.println("메시지: " + deletedMessage.getContent());
        }
    }
}
