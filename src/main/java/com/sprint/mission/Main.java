package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFChannel;
import com.sprint.mission.discodeit.service.jcf.JCFMessage;
import com.sprint.mission.discodeit.service.jcf.JCFUser;

import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        userServiceTest();

        messageServiceTest();

        channelServiceTest();
    }

    private static void channelServiceTest() {
        // ChannelService 테스트
        JCFChannel channelService = new JCFChannel();
        System.out.println("\n=== Channel Service ===");

        // 채널 생성
        Channel channel1 = channelService.createChannel("KBS");
        Channel channel2 = channelService.createChannel("EBS");
        System.out.println("채널 등록1: " + channel1.getChannelName());
        System.out.println("채널 등록2: " + channel2.getChannelName());

        // 채널 단건 조회
        Optional<Channel> foundChannel = channelService.getChannel(channel1.getId());
        foundChannel.ifPresent(c -> System.out.println("채널 조회 단건: " + c.getChannelName()));

        // 채널 다건 조회
        channelService.getChannels().forEach(c -> System.out.println("채널 조회 다건: " + c.getChannelName()));

        // 채널 수정
        channelService.updateChannel(channel1.getId(), "KBS Updated");
        System.out.println("채널 수정");
        Optional<Channel> updatedChannel = channelService.getChannel(channel1.getId());
        updatedChannel.ifPresent(c -> System.out.println("채널 수정 성공: " + c.getChannelName()));

        // 채널 삭제
        System.out.println("채널 삭제");
        Optional<Channel> deletedChannel = channelService.deleteChannel(channel1.getId());
        deletedChannel.ifPresent(c -> System.out.println("채널 삭제 성공: " + c.getChannelName()));

        // 삭제 여부 확인
        System.out.println("조회를 통해 삭제되었는지 확인");
        channelService.getChannels().forEach(c -> System.out.println("채널 조회 다건: " + c.getChannelName()));
    }

    private static void messageServiceTest() {
        // MessageService 테스트
        JCFMessage messageService = new JCFMessage();
        System.out.println("\n=== Message Service ===");

        // 메시지 생성
        Message message1 = messageService.createMessage("Hello");
        Message message2 = messageService.createMessage("World");
        System.out.println("메시지 등록1: " + message1.getText());
        System.out.println("메시지 등록2: " + message2.getText());

        // 메시지 조회 단건
        Optional<Message> foundMessage = messageService.getMessage(message1.getId());
        foundMessage.ifPresent(m -> System.out.println("메시지 조회 단건: " + m.getText()));

        // 메시지 조회 다건
        messageService.getMessages().forEach(m -> System.out.println("메시지 조회 다건: " + m.getText()));

        // 메시지 수정
        messageService.updateMessage(message1.getId(), "Hello Updated");
        System.out.println("메시지 수정");
        Optional<Message> updatedMessage = messageService.getMessage(message1.getId());
        updatedMessage.ifPresent(m -> System.out.println("메시지 수정 성공: " + m.getText()));

        // 메시지 삭제
        System.out.println("메시지 삭제");
        Optional<Message> deletedMessage = messageService.deleteMessage(message1.getId());
        deletedMessage.ifPresent(m -> System.out.println("메시지 삭제 성공: " + m.getText()));

        // 삭제 여부 확인
        System.out.println("조회를 통해 삭제되었는지 확인");
        messageService.getMessages().forEach(m -> System.out.println("메시지 조회 다건: " + m.getText()));
    }

    private static void userServiceTest() {
        // UserService 테스트
        JCFUser userService = new JCFUser();
        System.out.println("\n=== User Service ===");
        // 유저 생성
        User user1 = userService.createUser("1");
        User user2 = userService.createUser("2");
        System.out.println("유저 등록1: " + user1.getUsername());
        System.out.println("유저 등록2: " + user2.getUsername());
        // 유저 조회 단건
        Optional<User> foundUser = userService.getUser(user1.getId());
        foundUser.ifPresent(u -> System.out.println("유저 조회 단건: " + u.getUsername()));
        // 유저 조회 다건
        userService.getUsers().forEach(u -> System.out.println("유저 조회 다건: " + u.getUsername()));
        // 유저 수정
        userService.updateUser(user1.getId(), "11");
        System.out.println("유저 수정");
        Optional<User> updatedUser = userService.getUser(user1.getId());
        updatedUser.ifPresent(u -> System.out.println("유저 수정 성공: " + u.getUsername()));
        // 유저 삭제
        System.out.println("유저 삭제");
        Optional<User> deletedUser = userService.deleteUser(user1.getId());
        deletedUser.ifPresent(u -> System.out.println("유저 삭제 성공: " + u.getUsername()));

        System.out.println("조회를 통해 삭제되었는지 확인");
        userService.getUsers().forEach(u -> System.out.println("유저 조회 다건: " + u.getUsername()));
    }
}
