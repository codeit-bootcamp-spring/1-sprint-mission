package com.spirnt.mission.discodeit;

import com.spirnt.mission.discodeit.entity.*;
import com.spirnt.mission.discodeit.jcf.*;
import com.spirnt.mission.discodeit.service.*;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DiscodeitApplication {
    public static void main(String[] args) {
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();

        System.out.println("사용자 등록 시작");

        List<User> users = List.of(
                new User("JohnDoe", "john.doe@example.com", "010-1234-5678", "Seoul, Korea", 30, "Reading", new ArrayList<>(List.of("Technology", "Gaming"))),
                new User("JaneDoe", "jane.doe@example.com", "010-5678-1234", "Busan, Korea", 28, "Traveling", new ArrayList<>(List.of("Photography", "Hiking"))),
                new User("Alice", "alice.wonderland@example.com", "010-8765-4321", "Incheon, Korea", 25, "Painting", new ArrayList<>(List.of("Art", "Design"))),
                new User("Bob", "bob.builder@example.com", "010-4321-8765", "Daegu, Korea", 35, "Building", new ArrayList<>(List.of("Construction", "DIY Projects"))),
                new User("Charlie", "charlie.choco@example.com", "010-5678-9876", "Jeju, Korea", 27, "Cooking", new ArrayList<>(List.of("Food", "Baking")))
        );

        for (User user : users) {
            userService.create(user);
        }

        System.out.println("사용자 등록 완료.");
        System.out.println("============================");


        System.out.println("모든 사용자 조회:");
        userService.readAll().forEach(user -> {
            System.out.println("username : " + user.getUsername() + " | Email : " + user.getEmail() + " | phoneNumber : " + user.getPhoneNumber() + " | Address : " + user.getAddr() + " | Age : " + user.getAge() + " | Hobby : " + user.getHobby() + " | Interest : " + user.getInterest());
        });
        System.out.println("============================");


        UUID user1Id = users.get(1).getId();
        User searchUserId = userService.read(user1Id);
        System.out.println("단일 사용자 조회(user1):");
        System.out.println("user1 의 userName : " + searchUserId.getUsername()  + " | Email : " + searchUserId.getEmail() + " | phoneNumber : " + searchUserId.getPhoneNumber() + " | Address : " + searchUserId.getAddr() + " | Age : " + searchUserId.getAge() + " | Hobby : " + searchUserId.getHobby() + " | Interest : " + searchUserId.getInterest());
        System.out.println("============================");


        System.out.println("사용자 수정:");
        userService.update(user1Id, new User("JohnDoe", "updated_user1@example.com", "01023499548", "Seoul, Korea", 30, "Reading", new ArrayList<>(List.of("Technology", "Gaming"))));
        System.out.println("============================");


        System.out.println("수정된 사용자 조회:");
        User modifyUser = userService.read(user1Id);
        System.out.println(modifyUser.getUsername() + " | " + modifyUser.getEmail() + " | " + modifyUser.getPhoneNumber() + " | " + modifyUser.getAddr() + " | " + modifyUser.getAge() + " | " + modifyUser.getHobby() + " | " + modifyUser.getInterest() + " | " + modifyUser.getCreatedAt() + " | " + modifyUser.getUpdatedAt());
        System.out.println("============================");


        System.out.println("사용자 삭제:");
        userService.delete(user1Id);
        userService.delete(user1Id);
        System.out.println("============================");


        System.out.println("삭제된 사용자 확인:");
        System.out.println(userService.read(user1Id)); // null 또는 삭제 확인 메시지 출력


        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>Channel>>>>>>>>>>>>>>>>>>>>>>>");
        List<Channel> channels = List.of(
                new Channel("SB_1_Sprint", "JAVA Spring BackEnd Developer discode",
                        new ArrayList<>(List.of(users.get(0), users.get(1))), users.get(1)),
                new Channel("JavaScript_TechTalk", "All about JavaScript and its frameworks",
                        new ArrayList<>(List.of(users.get(1), users.get(2))), users.get(2)),
                new Channel("React_FrontEnd", "Front-end technologies focusing on React",
                        new ArrayList<>(List.of(users.get(3), users.get(4))), users.get(4)),
                new Channel("NodeJS_DevGroup", "Node.js development and deployment discussions",
                        new ArrayList<>(List.of(users.get(0), users.get(2))), users.get(0)),
                new Channel("SpringBoot_Dev", "Discussions on SpringBoot applications and microservices",
                        new ArrayList<>(List.of(users.get(1), users.get(4))), users.get(1))
        );

        channels.forEach(channel -> {
            channelService.create(channel);
        });
        System.out.println("채널 등록 완료");
        System.out.println("================");

        System.out.println("전체 채널 조회 :");
        channelService.readAll().forEach(channel -> System.out.println(channel.getChannelName()));
        System.out.println("================");

        Channel channel1 = channels.get(1);

        User addUser = new User("martin", "martin@example.com", "010-5678-5678", "Seoul, Korea", 30, "Reading", new ArrayList<>(List.of("Technology", "computer")));
        userService.create(addUser);

        System.out.println("채널에 새로운 맴버 추가하기 : ");
        channelService.channelMemberJoin(channel1.getId(), addUser);
        System.out.println("================");

        System.out.println("채널 주인장 변경하기 :");
        channelService.channelOwnerChange(channel1.getId(), users.get(1));
        System.out.println("================");


    }
}
