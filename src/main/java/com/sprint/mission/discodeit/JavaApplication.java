package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.*;

public class JavaApplication {

    public static void main(String[] args) {
        /**
         * User 실행
         */
        // JCFUserService 인스턴스 생성
        UserService userService = JCFUserService.getJCFUserService();

        // 유저 등록
        userService.craete("test1@test.com", "12345678", "name", "nickname", "010-1224-1234");
        userService.craete("test2@test.com", "12345678", "name", "nickname", "010-1284-1234");
        userService.craete("test3@test.com", "12345678", "name", "nickname", "010-1284-1234");

        // 테스트용 uuid
        UUID UserTestUuid1 = userService.allRead().get(0).getId();
        UUID UserTestUuid2 = userService.allRead().get(1).getId();
        UUID UserTestUuid3 = userService.allRead().get(2).getId();

        // 유저 출력(단건)
        displayUserInfo(userService.read(UserTestUuid1));

        // 전체 유저 출력
        displayAllUserInfo(userService.allRead());


        // 수정
        // 이메일 수정
        userService.updateEmail(UserTestUuid1, "testUpdate@test.com");
        // 수정된 데이터 조회
        System.out.println("\n이메일 수정");
        displayUserInfo(userService.read(UserTestUuid1));

        // 비밀번호 수정
        userService.updatePw(UserTestUuid1, "10203040");
        // 수정된 데이터 조회
        System.out.println("\n비밀번호 수정");
        displayUserInfo(userService.read(UserTestUuid1));

        // 이름 수정
        userService.updateName(UserTestUuid1, "test2");
        // 수정된 데이터 조회
        System.out.println("\n이름 수정");
        displayUserInfo(userService.read(UserTestUuid1));

        // 닉네임 수정
        userService.updateNickname(UserTestUuid1, "nicknametest");
        // 수정된 데이터 조회
        System.out.println("\n닉네임 수정");
        displayUserInfo(userService.read(UserTestUuid1));

        // 전화번호 수정
        userService.updatePhoneNumber(UserTestUuid1, "010-1111-1111");
        // 수정된 데이터 조회
        System.out.println("\n전화번호 수정");
        displayUserInfo(userService.read(UserTestUuid1));


//        // 유저 삭제
//        userService.delete(UserTestUuid1);
//        // 삭제 확인
//        System.out.println("\n삭제 후 >>> \n");
//        displayAllUserInfo(userService.allRead());


        /**
         * Channel 실행
         */
        // JCFChannelService 인스턴스 생성
        ChannelService channelService = JCFChannelService.getJCFChannelService();

        // 채널 생성
        channelService.craete(UserTestUuid1, "testCategory1", "test1", "explain");
        channelService.craete(UserTestUuid3, "testCategory3", "    test2    ", "설명~");
        channelService.craete(UserTestUuid1, "testCategory12", "    test2    ", "설명~");
        channelService.craete(UserTestUuid2, "testCategory2", "    test2    ", "설명~");

        // 테스트용 uuid
        UUID channelTestUuid1 = channelService.allRead().get(0).getId();    // 채널 서비스 테스트용 uuid
        UUID channelTestUuid2 = channelService.allRead().get(1).getId();    // 채널 서비스 테스트용 uuid
        UUID channelTestUuid3 = channelService.allRead().get(2).getId();    // 채널 서비스 테스트용 uuid

        // 채널 단건 조회
        System.out.println("\n\n 채널 단건 조회 테스트");
        displayChannel(channelTestUuid1, channelService.read(channelTestUuid1));

        // 채널 전체 조회
        System.out.println("\n\n 채널 전체 조회 테스트");
        displayAllChannel(channelService.allRead());

        // 채널 카테고리 수정
        channelService.updateCategory(channelTestUuid1, "updateCategory");
        System.out.println("\n\n 채널 카테고리 수정 테스트");
        displayAllChannel(channelService.allRead());

        // 채널 이름 수정
        channelService.updateName(channelTestUuid1, "testUpdate");
        System.out.println("\n\n 채널 이름 수정 테스트");
        displayAllChannel(channelService.allRead());

        // 채널 설명 수정
        channelService.updateExplanation(channelTestUuid1, "testUpdate");
        System.out.println("\n\n 채널 설명 수정 테스트");
        displayAllChannel(channelService.allRead());

        // 멤버 수정
        displayChannel( channelTestUuid1, channelService.read(channelTestUuid1));
        channelService.updateMembers(channelTestUuid1, UserTestUuid2);
        channelService.updateMembers(channelTestUuid1, UserTestUuid3);
        System.out.println("\n\n 채널 멤버 수정 테스트");
        displayChannel( channelTestUuid1, channelService.read(channelTestUuid1));

//        // 채널 삭제
//        channelService.delete(channelTestUuid1);
//        System.out.println("\n\n 채널 삭제 테스트");
//        displayAllChannel(channelService.allRead());


        /**
         * Message 실행
         */
        // JCFMessageService 인스턴스 생성
        MessageService messageService = new JCFMessageService();

        // 메시지 생성
        messageService.craete(channelTestUuid1, "first msg", UserTestUuid1);
        messageService.craete(channelTestUuid1, "second msg", UserTestUuid1);
        messageService.craete(channelTestUuid1, "third msg", UserTestUuid1);

        // 테스트용 uuid
        UUID messageTestUuid1 = messageService.allRead().get(0).getId();
        UUID messageTestUuid2 = messageService.allRead().get(1).getId();
        UUID messageTestUuid3 = messageService.allRead().get(2).getId();

        // 메시지 1건 조회
        System.out.println("\n\n 메시지 1건 조회 테스트");
        displayMessage(messageService.read(messageTestUuid2));

        // 메시지 특정 채널 전체 조회
        System.out.println("\n\n 메시지 특정 채널 전체 조회 테스트");
        displayAllMessage(channelTestUuid1, messageService.allRead());

        // 메시지 전체 조회
        System.out.println("\n\n 메시지 전체 조회 테스트");
        displayAllMessage(messageService.allRead());

        // 메시지 수정
        System.out.println("\n\n 메시지 수정 테스트");
        messageService.updateMessage(messageTestUuid2, "updateTest");
        displayAllMessage(messageService.allRead());

        // 메시지 삭제
        System.out.println("\n\n 메시지 삭제 테스트");
        messageService.delete(messageTestUuid3);
        displayAllMessage(messageService.allRead());

    }


    // 단일 사용자 정보 출력
    public static void displayUserInfo(User user) {
        System.out.println(
                "이메일: " + user.getEmail()
                        + ", 이름: " + user.getName()
                        + ", 닉네임: " + user.getNickname()
                        + ", 전화번호: " + user.getPhoneNumber() + "\n"
        );
    }

    // 모든 사용자 정보 출력
    public static void displayAllUserInfo(List<User> data) {
        data.stream()
                // 정렬 시 @ 앞까지만 잘라서 오름차순
                .sorted(Comparator.comparing(user -> user.getEmail().split("@")[0]))
                .forEach(user -> {
                    System.out.println(
                            "이메일: " + user.getEmail()
                                    + ", 이름: " + user.getName()
                                    + ", 닉네임: " + user.getNickname()
                                    + ", 전화번호: " + user.getPhoneNumber() + "\n"
                    );
                });
    }

    // 채널 1개 출력
    public static void displayChannel(UUID channelId, Channel channel) {
        if (channel == null) {
            System.out.println("채널 없음");
            return;
        }

        UserService userService = JCFUserService.getJCFUserService();

        System.out.print(
                "채널 주인: " + userService.read(channel.getOwnerId()).getEmail()
                        + ", 카테고리: " + channel.getCategory()
                        + ", 채널명: " + channel.getName()
                        + ", 채널 설명: " + channel.getExplanation()
                        + ", 멤버: "
        );

        channel.getMembers().stream()
                .forEach(member -> {
                    System.out.print(userService.read(member).getEmail() + " ");
                });

        System.out.println();
    }

    // 모든 유저 모든 채널 출력
    public static void displayAllChannel(List<Channel> channels) {

        UserService userService = JCFUserService.getJCFUserService();

        channels.stream()
                .sorted(Comparator.comparing((Channel channel) -> userService.read(channel.getOwnerId()).getEmail().split("@")[0])
                        .thenComparing(Channel::getCategory)
                        .thenComparing(Channel::getName))
                .forEach(channel -> {
                    System.out.print(
                            "채널 주인: " + userService.read(channel.getOwnerId()).getEmail()
                                    + ", 카테고리: " + channel.getCategory()
                                    + ", 채널명: " + channel.getName()
                                    + ", 채널 설명: " + channel.getExplanation()
                                    + ", 멤버: "
                    );

                    channel.getMembers().stream()
                            .forEach(member -> {
                                System.out.print(userService.read(member).getEmail() + " ");
                            });

                    System.out.println();
                });
    }

    // 메시지 1건 조회
    public static void displayMessage(Message message) {

        UserService userService = JCFUserService.getJCFUserService();
        ChannelService channelService = JCFChannelService.getJCFChannelService();

        System.out.println(
                "채널: " + channelService.read(message.getChannelId()).getName()
                        + ", 작성자: " + userService.read(message.getWriter()).getEmail()
                        + ", 내용: " + message.getMessage()
        );

    }

    // 메시지 전체 조회
    public static void displayAllMessage(List<Message> messages) {

        if (messages == null){
            System.out.println("메시지 없음");
        }

        UserService userService = JCFUserService.getJCFUserService();
        ChannelService channelService = JCFChannelService.getJCFChannelService();

        messages.stream()
                .forEach(message -> {
                    System.out.println(
                            "채널: " + channelService.read(message.getChannelId()).getName()
                                    + ", 작성자: " + userService.read(message.getWriter()).getEmail()
                                    + ", 내용: " + message.getMessage()
                    );
                });
    }

    // 특정 채널 메시지 전체 조회
    public static void displayAllMessage(UUID channelId, List<Message> messages) {
        
        if (messages == null){
            System.out.println("메시지 없음");
        }
        
        UserService userService = JCFUserService.getJCFUserService();
        ChannelService channelService = JCFChannelService.getJCFChannelService();

        messages.stream()
                .filter(message -> channelService.read(message.getChannelId()).getId().equals(channelId))
                .forEach(message -> {
                    System.out.println(
                            "채널: " + channelService.read(message.getChannelId()).getName()
                                    + ", 작성자: " + userService.read(message.getWriter()).getEmail()
                                    + ", 내용: " + message.getMessage()
                    );
                });
    }
}