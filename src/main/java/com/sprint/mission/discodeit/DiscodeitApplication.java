package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.jcf.*;
import com.sprint.mission.discodeit.service.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class DiscodeitApplication {
    public static void main(String[] args) {
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();
        MessageService messageService = new JCFMessageService();

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
        userService.readAll();
        System.out.println("============================");


        UUID user1Id = users.get(1).getId();
        System.out.println("단일 사용자 조회(user1):");
        userService.read(user1Id);
        System.out.println("============================");

        System.out.println("사용자 수정:");
        userService.update(user1Id, new User("JohnDoe", "updated_user1@example.com", "010-2349-9548", "Seoul, Korea", 30, "Reading", new ArrayList<>(List.of("Technology", "Gaming"))));
        System.out.println("============================");

        System.out.println("사용자 수정(휴대폰번호 에러):");
        userService.update(user1Id, new User("JohnDoe", "updated_user1@example.com", "01023499548", "Seoul, Korea", 30, "Reading", new ArrayList<>(List.of("Technology", "Gaming"))));
        System.out.println("============================");

        System.out.println("사용자 수정(ID 에러):");
        UUID errorId = UUID.randomUUID();
        userService.update(errorId, new User("JohnDoe", "updated_user1@example.com", "01023499548", "Seoul, Korea", 30, "Reading", new ArrayList<>(List.of("Technology", "Gaming"))));
        System.out.println("============================");


        System.out.println("수정된 사용자 조회:");
        User modifyUser = userService.read(user1Id);
        System.out.println(modifyUser.getUsername() + " | " + modifyUser.getEmail() + " | " + modifyUser.getPhoneNumber() + " | " + modifyUser.getAddr() + " | " + modifyUser.getAge() + " | " + modifyUser.getHobby() + " | " + modifyUser.getInterest() + " | " + modifyUser.getCreatedAt() + " | " + modifyUser.getUpdatedAt());
        System.out.println("============================");


        System.out.println("사용자 삭제:");
        userService.delete(user1Id);
        System.out.println("============================");

        System.out.println("삭제된 사용자 삭제:");
        userService.delete(user1Id);
        System.out.println("============================");


        System.out.println("삭제된 사용자 확인:");
        userService.read(user1Id); // null 또는 삭제 확인 메시지 출력


        System.out.println();
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
        channelService.readAll();
        System.out.println("================");

        Channel channel1 = channels.get(1);

        User addUser = new User("martin", "martin@example.com", "010-5678-5678", "Seoul, Korea", 30, "Reading", new ArrayList<>(List.of("Technology", "computer")));
        userService.create(addUser);
        System.out.println("================");

        System.out.println("채널에 새로운 맴버 추가하기 : ");
        channelService.channelMemberJoin(channel1.getId(), addUser);
        System.out.println("================");

        System.out.println("채널 주인장 변경하기 :");
        channelService.channelOwnerChange(channel1.getId(), users.get(1));
        System.out.println("================");


        System.out.println();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>Message>>>>>>>>>>>>>>>>>>>>>>>");

        List<Message> messages = List.of(
            new Message("Hello", users.get(1), users.get(4)),
            new Message("처음 뵙겠습니다~", users.get(1), channels.get(1)),
            new Message("안녕하세요!", users.get(2), channels.get(0)),
            new Message("채널 가입 요청 드립니다.", users.get(3), channels.get(2)),
            new Message("반가워요!", users.get(1), users.get(3)),
            new Message("공지사항 확인 부탁드립니다.", users.get(0), channels.get(3)),
            new Message("다음 모임은 언제인가요?", users.get(4), channels.get(1)),
            new Message("메일 보내드렸습니다.", users.get(2), users.get(0)),
            new Message("새 프로젝트에 대해서 논의해보아요.", users.get(3), channels.get(4)),
            new Message("다른 문의사항 있으면 알려주세요.", users.get(2), channels.get(0))
        );

        messages.forEach(message -> {
            messageService.create(message);
        });

        System.out.println("메시지 전송 완료");
        System.out.println("================");

        System.out.println("전체 메시지 조회 : ");
        messageService.readAll();
        System.out.println("================");

        System.out.println(channels.get(1).getChannelName() + " 채널 전체 메시지 조회 : ");
        messageService.readAll(channels.get(1).getId());

    }
}
