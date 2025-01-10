package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;

import java.util.*;
public class JavaApplication {
    public static void main(String[] args) {
        // 데이터 저장소 초기화
        Map<UUID, User> userData = new HashMap<>();
        Map<UUID, Channel> channelData = new HashMap<>();
        Map<UUID, Message> messageData = new HashMap<>();

        // 서비스 구현체 초기화
        JCFMessageService messageService = new JCFMessageService(messageData, null, null);
        JCFUserService userService = new JCFUserService(userData, messageService);
        JCFChannelService channelService = new JCFChannelService(channelData, userService, messageService);
        messageService = new JCFMessageService(messageData, userService, channelService);

        // === User Service 테스트 ===
        System.out.println("=== 사용자 서비스 테스트 ===");

        // 1. 사용자 등록
        User user1 = new User("user01", "password123", "크리스", "chris0123@gmail.com");
        User user2 = new User("user02", "password456", "밥", "bob060@gmail.com");
        User user3 = new User("user03", "password789", "엘리", "ellie123@gmail.com");
        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);

        System.out.println("\n[사용자 등록 완료]");

        // 2. 사용자 조회 (단건)
        System.out.println("\n[단일 사용자 조회]");
        Optional<User> retrievedUser = userService.readUser(user1);
        retrievedUser.ifPresent(user -> System.out.println("조회된 사용자: " + user.getUsername()));

        // 3. 사용자 조회 (다건)
        System.out.println("\n[모든 사용자 조회]");
        List<User> allUsers = userService.readAllUsers();
        allUsers.forEach(user -> System.out.println("사용자: " + user.getUsername()));

        // 4. 사용자 수정
        System.out.println("\n[사용자 수정]");
        User updatedUser1 = new User("user01", "password123", "크리스지롱", "chris0123@gmail.com");
        userService.updateUser(user1, updatedUser1);
        System.out.println("수정 완료");

        // 5. 수정된 사용자 조회
        System.out.println("\n[수정된 사용자 조회]");
        Optional<User> updatedRetrievedUser = userService.readUser(user1);
        updatedRetrievedUser.ifPresent(user -> System.out.println("수정된 사용자: " + user.getUsername()));

        // 6. 사용자 삭제
        System.out.println("\n[사용자 삭제]");
        boolean isDeleted = userService.deleteUser(user1);
        System.out.println("삭제 성공 여부: " + isDeleted);

        // 7. 삭제된 사용자 확인
        System.out.println("\n[삭제된 사용자 확인]");
        Optional<User> deletedUserCheck = userService.readUser(user1);
        System.out.println(deletedUserCheck.isPresent() ? "삭제 실패" : "삭제 성공");

        // === Channel Service 테스트 ===
        System.out.println("\n=== 채널 서비스 테스트 ===");

        // 1. 채널 등록
        System.out.println("\n[채널 등록]");
        Channel channel1 = new Channel("General", "General discussion", new HashSet<>(), new ArrayList<>());
        Channel channel2 = new Channel("Tech Talk", "Discuss latest tech trends", new HashSet<>(), new ArrayList<>());

        channelService.createChannel(channel1);
        channelService.createChannel(channel2);

        // 2. 채널에 참가자 추가
        System.out.println("\n[채널에 참가자 추가]");
        channelService.addParticipantToChannel(channel1, user2); // 밥 추가
        channelService.addParticipantToChannel(channel2, user3); // 엘리 추가

        System.out.println("참가자 추가 완료");

        // === Message Service 테스트 ===
        System.out.println("\n=== 메시지 서비스 테스트 ===");

        // 1. 메시지 생성
        System.out.println("\n[메시지 생성]");
        Message message1 = new Message("안녕하세요, 밥입니다.",user2, channel1); // 밥은 채널1 참가자
        Message message2 = new Message("안녕하세요, 엘리입니다.",user3, channel2); // 엘리는 채널2 참가자

        messageService.createMessage(message1);
        messageService.createMessage(message2);

        // 2. 메시지 조회 (단건)
        System.out.println("\n[단일 메시지 조회]");
        Optional<Message> retrievedMessage = messageService.readMessage(message1);
        retrievedMessage.ifPresent(message ->
                System.out.println("조회된 메시지: " + message.getContent()));

        // 3. 메시지 조회 (다건)
        System.out.println("\n[모든 메시지 조회]");
        List<Message> allMessages = messageService.readAll();
        allMessages.forEach(message ->
                System.out.println("메시지: " + message.getContent()));

        // 4. 메시지 수정
        System.out.println("\n[메시지 수정]");
        try {
            Message updatedMessage1 = new Message("수정된 메시지입니다.",user2, channel1);
            messageService.updateByAuthor(user2, updatedMessage1);
            System.out.println("메시지 수정 완료");
        } catch (NoSuchElementException e) {
            System.out.println("메시지 수정 중 오류 발생: " + e.getMessage());
        }

        // 5. 수정된 메시지 조회
        System.out.println("\n[수정된 메시지 조회]");
        Optional<Message> updatedRetrievedMessage = messageService.readMessage(message2);
        updatedRetrievedMessage.ifPresent(message ->
                System.out.println("수정된 메시지: " + message.getContent()));

        // 6. 메시지 삭제
        System.out.println("\n[메시지 삭제]");
        boolean isMessageDeleted = messageService.deleteMessage(message1);
        System.out.println("메시지 삭제 성공 여부: " + isMessageDeleted);

        // 7. 삭제된 메시지 확인
        System.out.println("\n[삭제된 메시지 확인]");
        Optional<Message> deletedMessageCheck = messageService.readMessage(message1);
        System.out.println(deletedMessageCheck.isPresent() ? "삭제 실패" : "삭제 성공");

        // 8. 검증 실패 테스트 (채널에 없는 사용자가 메시지를 생성하려고 할 때)
        System.out.println("\n[검증 실패 테스트]");
        try {
            Message invalidMessage = new Message("이 채널에 속하지 않은 사용자입니다.",user1, channel1); // user1은 채널 참가자가 아님
            messageService.createMessage(invalidMessage);
        } catch (IllegalArgumentException e) {
            System.out.println("예외 발생: " + e.getMessage());
        }
    }
}
