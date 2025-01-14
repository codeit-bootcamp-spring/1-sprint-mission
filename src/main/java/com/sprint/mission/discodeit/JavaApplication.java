package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.jcf.*;

import java.time.Instant;

public class JavaApplication {
    public static void main(String[] args) {
        // 1. 싱글톤 서비스 인스턴스 생성
        JCFUserService userService = JCFUserService.getInstance();
        JCFChannelService channelService = JCFChannelService.getInstance();
        JCFMessageService messageService = JCFMessageService.getInstance(userService, channelService);

        // 2. 사용자 등록
        System.out.println("== 사용자 등록 ==");
        User user1 = new User("Alice", "alice@example.com");
        User user2 = new User("Bob", "bob@example.com");
        userService.create(user1);
        userService.create(user2);
        System.out.printf("User: %s (CreatedAt: %s)%n", user1.getUsername(), Instant.ofEpochMilli(user1.getCreatedAt()));
        System.out.printf("User: %s (CreatedAt: %s)%n", user2.getUsername(), Instant.ofEpochMilli(user2.getCreatedAt()));

        // 3. 사용자 수정
        System.out.println("\n== 사용자 수정 ==");
        user1.updateUsername("Alice Updated");
        userService.update(user1.getId(), user1);
        System.out.printf("Updated User: %s (UpdatedAt: %s)%n", user1.getUsername(), Instant.ofEpochMilli(user1.getUpdateAT()));

        // 4. 사용자 삭제 후 조회
        System.out.println("\n== 사용자 삭제 ==");
        userService.delete(user1.getId());
        userService.read(user1.getId()).ifPresentOrElse(
                user -> System.out.println("Deleted User Found: " + user),
                () -> System.out.println("삭제된 사용자입니다.")
        );

        // 5. 채널 등록
        System.out.println("\n== 채널 등록 ==");
        Channel channel1 = new Channel("testChannel", "General discussion channel", user2);
        Channel channel2 = new Channel("test2Channel", "Random discussion channel", user2);
        channelService.create(channel1);
        channelService.create(channel2);
        System.out.printf("Channel: %s (CreatedAt: %s)%n", channel1.getName(), Instant.ofEpochMilli(channel1.getCreatedAt()));
        System.out.printf("Channel: %s (CreatedAt: %s)%n", channel2.getName(), Instant.ofEpochMilli(channel2.getCreatedAt()));

        // 6. 채널 수정
        System.out.println("\n== 채널 수정 ==");
        channel1.updateDescription("Updated General discussion channel");
        channelService.update(channel1.getId(), channel1);
        System.out.printf("Updated Channel: %s (UpdatedAt: %s)%n", channel1.getName(), Instant.ofEpochMilli(channel1.getUpdateAT()));

        // 7. 채널 삭제 후 조회
        System.out.println("\n== 채널 삭제 ==");
        channelService.delete(channel1.getId());
        channelService.read(channel1.getId()).ifPresentOrElse(
                channel -> System.out.println("Deleted Channel Found: " + channel),
                () -> System.out.println("삭제된 채널입니다.")
        );

        // 8. 메시지 등록
        System.out.println("\n== 메시지 등록 ==");
        Message message1 = new Message("test_message", user2.getId(), channel2.getId());
        Message message2 = new Message("test_message2", user2.getId(), channel2.getId());
        messageService.create(message1);
        messageService.create(message2);
        System.out.printf("Message: %s (CreatedAt: %s)%n", message1.getContent(), Instant.ofEpochMilli(message1.getCreatedAt()));
        System.out.printf("Message: %s (CreatedAt: %s)%n", message2.getContent(), Instant.ofEpochMilli(message2.getCreatedAt()));

        // 9. 메시지 수정
        System.out.println("\n== 메시지 수정 ==");
        message1.updateContent("Updated test_message");
        messageService.update(message1.getId(), message1);
        System.out.printf("Updated Message: %s (UpdatedAt: %s)%n", message1.getContent(), Instant.ofEpochMilli(message1.getUpdateAT()));

        // 10. 메시지 삭제 후 조회
        System.out.println("\n== 메시지 삭제 ==");
        messageService.delete(message1.getId());
        messageService.read(message1.getId()).ifPresentOrElse(
                message -> System.out.println("Deleted Message Found: " + message),
                () -> System.out.println("삭제된 메시지입니다.")
        );

        // 11. 전체 조회
        System.out.println("\n== 전체 조회 ==");
        System.out.println("Users: ");
        userService.readAll().forEach(user ->
                System.out.printf("  - %s (CreatedAt: %s)%n", user.getUsername(), Instant.ofEpochMilli(user.getCreatedAt()))
        );

        System.out.println("Channels: ");
        channelService.readAll().forEach(channel ->
                System.out.printf("  - %s (Description: %s, Created by: %s, CreatedAt: %s)%n",
                        channel.getName(), channel.getDescription(), channel.getCreator().getUsername(), Instant.ofEpochMilli(channel.getCreatedAt()))
        );

        System.out.println("Messages: ");
        messageService.readAll().forEach(message ->
                System.out.printf("  - %s (Sent by: %s, Channel: %s, CreatedAt: %s)%n",
                        message.getContent(), userService.read(message.getSenderId()).map(User::getUsername).orElse("Unknown"),
                        channelService.read(message.getChannelId()).map(Channel::getName).orElse("Unknown"),
                        Instant.ofEpochMilli(message.getCreatedAt()))
        );
    }
}
