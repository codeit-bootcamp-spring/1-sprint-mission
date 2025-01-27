package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.Message;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.factory.service.FileServiceFactory;
import com.sprint.mission.discodeit.factory.service.ServiceFactory;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.ChannelType;
import com.sprint.mission.discodeit.util.FileIOUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class JavaApplication {
    public static void main(String[] args) {
/*        Factory factory = JCFFactory.getInstance();*/
        ServiceFactory factory = FileServiceFactory.getInstance();
        UserService userService = factory.getUserService();
        ChannelService channelService = factory.getChannelService();
        MessageService messageService = factory.getMessageService();

        try {
            Files.deleteIfExists(Paths.get("./result/users.ser"));
            Files.deleteIfExists(Paths.get("./result/messages.ser"));
            Files.deleteIfExists(Paths.get("./result/channels.ser"));
        } catch (IOException e) {
            System.err.println("파일 초기화 중 오류 발생: " + e.getMessage());
        }
        System.out.println("<회원 생성하기>");
        User user1 = userService.create("김민준", "010-1111-1111", "Abcdefgh12312!");
        User user2 = userService.create("이서윤", "010-2222-1111", "Abcdefgh12312!!");
        User user3 = userService.create("박지훈", "010-3333-1111", "Abcdefgh12312!!");
        User user4 = userService.create("이채은", "010-4444-1111", "Abcdefgh12312!!");
        User user5 = userService.create("정다은", "010-5555-1111", "Abcdefgh12312!!");
        System.out.println(" - 회원 생성 시 발생할 수 있는 문제");
        System.out.print("    - 1. 동일한 전화번호로 회원 가입을 진행 : ");
        try {
            User user1_1 = userService.create("홍지훈", "010-1111-1111", "Abcdefgh!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.print("    - 2. 올바르지 않은 전화번호 형식 : ");
        try {
            User unCorrectPhone = userService.create("김철수", "010-12345-12345", "Abcdefgh!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.print("    - 3. 올바르지 않은 비밀번호 형식 : ");
        try {
            User unCorrectPass = userService.create("김영미", "010-1234-4321", "1234");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("회원 생성 시 문제가 발생하면 저장되지 않음을 확인: " + userService.getAllUser());
        System.out.println();
        System.out.println("===============================================================================");
        System.out.println();
        System.out.println("<회원 전체 조회하기>");
        List<User> users = userService.getAllUser();
        System.out.println(users);
        System.out.println();
        System.out.println("<단일 회원 조회하기>");
        Optional<User> findUser1 = userService.getUserByPhone("010-1111-1111");
        System.out.println("findUser : " + findUser1);
        System.out.println();
        System.out.println("===============================================================================");
        System.out.println();
        System.out.println("<회원 비밀번호 업데이트 하기>");
        userService.updateUserPassword(user1, "Zdefdasdf!@");
        System.out.println("UpdatePassword : " + user1);
        System.out.println();
        System.out.println("===============================================================================");
        System.out.println();
        System.out.println("<회원 정보 삭제하기>");
        userService.delete(user1);
        System.out.println(userService.getAllUser());
        System.out.print(" - 회원 삭제 시 저장되어 있지 않는 회원일 경우 문제가 발생 : ");
        try {
            User unRegisterUser = new User("김미영", "010-4343-3434", "Rasdffa1234!");
            userService.delete(unRegisterUser);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println();
        System.out.println("===============================================================================");
        System.out.println();
        System.out.println("<채널 생성하기>");
        Channel channel1 = channelService.create("코드잇 디스코드", "코드잇용 디스코드 채널입니다.", ChannelType.TEXT);
        Channel channel2 = channelService.create("코테 준비", "코테 준비용 채널입니다.", ChannelType.VOICE);
        messageService.create("열심히 공부하는 코테 준비 채널입니다!", user4, channel2);
        messageService.create("열심히 하겠습니다!!", user3, channel2);
        User channelCreator = userService.create("김자바", "010-8739-9343", "Abcdefgh!");
        Channel javaChannel = channelService.create("자바 공부합시다", "자바 공부용 채널입니다.", ChannelType.TEXT);
        System.out.println(channel1);
        System.out.println(channel2);
        System.out.println(" - 채널 생성 시 발생할 수 있는 문제");
        System.out.print("     - 기존 채널과 동일한 이름으로 채널을 개설하는 경우 : ");
        try {
            Channel sameChannel = channelService.create("코드잇 디스코드", "이 방도 코드잇 디스코드 방입니다.", ChannelType.VOICE);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("<채널 설명 변경하기>");
        channelService.updateDescription(channel1, "코드잇 디스코드 채널에 오신 것을 환영합니다.");
        System.out.println(channel1);
        System.out.println("<채널 타입 변경하기>");
        channelService.updateType(channel1, ChannelType.VOICE);
        System.out.println(channel1);
        System.out.println();
        System.out.println("===============================================================================");
        System.out.println();
        System.out.println("<채널 단일 조회하기>");
        System.out.println("코드잇 디스코드 채널 찾기 : " + channelService.getChannelByName("코드잇 디스코드"));
        System.out.println("<전체 채널 조회하기>");
        System.out.println(channelService.getAllChannel());
        System.out.println();
        System.out.println("===============================================================================");
        System.out.println();
        System.out.println("<메시지 생성하기>");
        Message message2_1 = messageService.create("안녕하세요 지훈님 저는 이서윤이라고 합니다.", user2, channel1);
        Message message2_2 = messageService.create("저는 요즘 스프링에 대해 공부하고 있습니다^^", user2, channel2);
        Message message3_1 = messageService.create("테스트 확인 용 메시지입니다!", user3, channel2);
        System.out.println(message2_1);
        Message message3 = messageService.create("네 만나서 반갑습니다!", user3, channel1);
        System.out.println(message3);
        System.out.println(" - 메시지 생성 시 발생할 수 있는 문제");
        System.out.print("    - 1. 내용이 작성되지 않은 메시지 : ");
        try {
            Message message4 = messageService.create("", user4, channel1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.print("    - 2. 등록되지 않은 회원이 메시지를 보낼 때 : ");
        User unRegistUser1 = new User("미등록회원", "010-1919-9191", "Abcdefgh!");
        try {
            Message unregistMessage = messageService.create("안녕하세요 만나서 반갑습니다", unRegistUser1, channel1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println();
        System.out.println("user2 = " + user2);
        System.out.println("===============================================================================");
        System.out.println();
        System.out.println("<특정 채널에서 전체 메시지 조회하기>");
        System.out.print("코드잇 채널 전체 메시지 조회");
        List<Message> allMessages = messageService.getMessageByChannel(channel1);
        System.out.println(allMessages);
        System.out.println();
        System.out.print("코테 준비 채널 전체 메시지 조회");
        List<Message> allMessages2 = messageService.getMessageByChannel(channel2);
        System.out.println(allMessages2);
        System.out.println("<단일 메시지 조회하기>");
        List<Message> getMessage = messageService.getMessageByUser(user2);
        System.out.println(getMessage);
        System.out.println();
        System.out.println("===============================================================================");
        System.out.println();
        System.out.println("<메시지 내용 수정하기>");
        messageService.updateMessageContent(message2_1, "메시지 내용 업데이트");
        System.out.println(message2_1);
        System.out.println();
        System.out.println("===============================================================================");
        System.out.println();
        System.out.println("<메시지 삭제하기>");
        messageService.deleteMessageByWriter(user2, message2_1.getId());
        System.out.println("전체 메시지 조회하기: " + messageService.getMessageByUser(user2));
        System.out.println("채널 삭제하기 : ");
        channelService.delete(channel1);
        System.out.println(channelService.getAllChannel());
        System.out.println();
        System.out.println("===============================================================================");
        System.out.println();
        if (factory instanceof FileServiceFactory) {
            FileIOUtil.convertDSerToJson(Paths.get("./result/users.ser"), Paths.get("./json/users.json"));
            FileIOUtil.convertDSerToJson(Paths.get("./result/messages.ser"), Paths.get("./json/messages.json"));
            FileIOUtil.convertDSerToJson(Paths.get("./result/channels.ser"), Paths.get("./json/channels.json"));
        }
    }
}