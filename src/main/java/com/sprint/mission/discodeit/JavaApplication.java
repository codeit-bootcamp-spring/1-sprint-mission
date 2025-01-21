package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.Scanner;

public class JavaApplication {

    public static User user1 = new User("user1@gmail.com", "user1", "1234");
    public static User user2 = new User("user2@gmail.com", "user2", "1234");
    public static User user3 = new User("user3@gmail.com", "user3", "1234");
    public static User user4 = new User("user4@gmail.com", "user4", "1234");
    public static User user5 = new User("user5@gmail.com", "user5", "1234");

    public static void main(String[] args) {
        userServiceTest();
        channelServiceTest();
        messageServiceTest();
    }

    private static void messageServiceTest() {
        Message message1 = new Message("안녕하세요 전 user1입니다.");
        Message message2 = new Message("안녕하세요 전 user2입니다.");
        Message message3 = new Message("안녕하세요 전 user3입니다.");
        Message message4 = new Message("안녕하세요 전 user4입니다.");
        Message message5 = new Message("안녕하세요 전 user5입니다.");

        MessageService messageService = new MessageService();

        System.out.println("=== 메세지 저장 단계 ===");
        messageService.save(message1);
        messageService.save(message2);
        messageService.save(message3);
        messageService.save(message4);
        messageService.save(message5);
        System.out.println();

        System.out.println("=== 메세지 아이디로 메세지 조회 단계 ===");
        messageService.findById(user1.getId());
        System.out.println();

        System.out.println("=== 전체 메세지 정보 조회 ===");
        messageService.findAll();
        System.out.println();

        System.out.println("=== 메세지 내용 정보 변경 단계 ===");
        message1.updateMessage("변경된 내용입니다");
        messageService.update(message1);
        // 변경됐는지 확인
        messageService.findById(message1.getId());
        System.out.println("변경된 시전 시간 : " + message1.getUpdatedAt());
        System.out.println();

        System.out.println("=== 메세지 삭제 단계 ===");
        messageService.delete(message1.getId());
        System.out.println("message1이 삭제됐는지 확인");
        messageService.findAll();
        System.out.println();
    }

    private static void channelServiceTest() {
        Channel channel1 = new Channel("채널1", user1);
        Channel channel2 = new Channel("채널2", user2);
        Channel channel3 = new Channel("채널3", user3);
        Channel channel4 = new Channel("채널4", user4);
        Channel channel5 = new Channel("채널5", user5);

        ChannelService channelService = new ChannelService();
        System.out.println("=== 채널 저장 단계 ===");
        channelService.save(channel1);
        channelService.save(channel2);
        channelService.save(channel3);
        channelService.save(channel4);
        channelService.save(channel5);
        System.out.println();

        System.out.println("=== 채널 아이디로 채널 조회 단계 ===");
        channelService.findById(channel1.getId());
        channelService.findById(channel2.getId());
        channelService.findById(channel3.getId());
        channelService.findById(channel4.getId());
        channelService.findById(channel5.getId());
        System.out.println();

        System.out.println("=== 전체 채널 정보 조회 ===");
        channelService.findAll();
        System.out.println();

        System.out.println("=== 채널 정보 변경 단계 ===");
        channel1.updateChannelName("channel1");
        channel1.updateAdminUser(user2);
        channelService.update(channel1);
        // 변경됐는지 확인 절차
        channelService.findById(channel1.getId());
        System.out.println("변경된 시점 시간 : " + channel1.getUpdatedAt());
        System.out.println();

        System.out.println("=== 채널 정보 삭제 단계 ===");
        channelService.delete(channel1.getId());
        System.out.println("channel1이 삭제 됐는지 확인");
        channelService.findAll();
        System.out.println();
    }

    private static void userServiceTest() {
        UserService userService = new UserService();
        System.out.println("=== 유저 저장 단계 ===");
        userService.save(user1);
        userService.save(user2);
        userService.save(user3);
        userService.save(user4);
        userService.save(user5);
        System.out.println();

        System.out.println("=== 유저 아이디로 조회 단계 ===");
        userService.findById(user1.getId());
        userService.findById(user2.getId());
        userService.findById(user3.getId());
        userService.findById(user4.getId());
        userService.findById(user5.getId());
        System.out.println();

        System.out.println("=== 전체 유저 찾기 단계 ===");
        userService.findAll();
        System.out.println();

        System.out.println("=== 유저 정보 변경 단계 ===");
        user1.updatePassword("newpassword1");
        user1.updateUserNickName("아무개씨");
        userService.update(user1); // 비밀번호 변경, 닉네임 변경
        // 변경됐는지 확인
        userService.findById(user1.getId());
        System.out.println("변경 시간 : " + user1.getUpdatedAt());
        System.out.println();

        System.out.println("=== 유저 정보 삭제 단계 ===");
        userService.delete(user1.getId());
        System.out.println("user1이 삭제 됐는지 확인.");
        userService.findAll();
        System.out.println();
    }
}
