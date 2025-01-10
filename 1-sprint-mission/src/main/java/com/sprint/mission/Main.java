package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        JCFMessageService messageService = new JCFMessageService();
        JCFChannelService channelService = new JCFChannelService();
        JCFUserService userService = new JCFUserService();

        // User 생성
        System.out.println("===== User 테스트 =====");
        User user1 = new User("Alice");
        User user2 = new User("Bob");
        userService.create(user1);
        userService.create(user2);
        System.out.println("생성된 User: " + user1.getName());
        System.out.println("생성된 User: " + user2.getName());

        // User 조회
        User retrievedUser = userService.read(user1.getId());
        System.out.println("조회된 User: " + retrievedUser.getName());

        // User 모두 조회
        List<User> users = userService.readAll();
        System.out.println("모든 User: " + users);

        // User 수정
        userService.update(user1.getId(), "David");
        System.out.println("수정된 User: " + userService.read(user1.getId()).getName());

        // User 삭제
        userService.delete(user1.getId());
        System.out.println("삭제 후 User 조회: " + userService.read(user1.getId()));

        // Channel 생성
        System.out.println("\n===== Channel 테스트 =====");
        Channel channel1 = new Channel("General");
        Channel channel2 = new Channel("Announcements");
        channelService.create(channel1);
        channelService.create(channel2);
        System.out.println("생성된 Channel: " + channel1.getName());
        System.out.println("생성된 Channel: " + channel2.getName());

        // Channel 조회
        Channel retrievedChannel = channelService.read(channel1.getId());
        System.out.println("조회된 Channel: " + retrievedChannel.getName());

        // Channel 모두 조회
        List<Channel> channels = channelService.readAll();
        System.out.println("모든 Channel: " + channels);

        // Channel 수정
        channelService.update(channel1.getId(), "News");
        System.out.println("수정된 Channel: " + channelService.read(channel1.getId()).getName());

        // Channel 삭제
        channelService.delete(channel1.getId());
        System.out.println("삭제 후 Channel 조회: " + channelService.read(channel1.getId()));

        // User-Channel-Message 연동 테스트
        System.out.println("\n===== User-Channel-Message 통합 테스트 =====");
        User user3 = new User("Charlie");
        User user4 = new User("Eve");
        userService.create(user3);
        userService.create(user4);

        Channel newChannel = new Channel("Tech");
        channelService.create(newChannel);

        // 메시지 전송
        Message message1 = new Message(user3, "Hello, Eve!");
        messageService.create(message1, newChannel);
        System.out.println("메시지 전송 완료: Hello, Eve!");

        Message message2 = new Message(user4, "Hello, Charlie!");
        messageService.create(message2, newChannel);
        System.out.println("메시지 전송 완료: Hello, Charlie!");

        // 채널에서 메시지 조회
        List<Message> messages = channelService.read(newChannel.getId()).getMessages();
        System.out.println("채널 메시지 조회: " + messages.size() + "개의 메시지");
        for (Message message : messages) {
            System.out.println(" (작성자: " + message.getUser().getName() + ") : " + message.getContent());
        }

        // 메시지 수정
        messageService.update(message1.getId(), "Hi, Eve!");
        System.out.println("수정된 메시지 내용: " + messageService.read(message1.getId()).getContent());

        // 메시지 삭제
        messageService.delete(message2.getId(), newChannel);
        System.out.println("삭제 후 메시지 조회: " + channelService.read(newChannel.getId()).getMessages().size() + "개의 메시지");

        // 삭제 후 채널에서 메시지 조회
        System.out.println("채널 메시지 조회: " + messages.size() + "개의 메시지");
        for (Message message : messages) {
            System.out.println(" (작성자: " + message.getUser().getName() + ") : " + message.getContent());
        }
    }
}