package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {
        // 1. Repository 인스턴스 생성
        FileUserRepository userRepository = new FileUserRepository(new File("users.db"));
        FileChannelRepository channelRepository = new FileChannelRepository(new File("channels.db"));
        FileMessageRepository messageRepository = new FileMessageRepository(new File("messages.db"));

        // 2. Service 인스턴스 생성
        UserService userService = new BasicUserService(userRepository);
        ChannelService channelService = new BasicChannelService(channelRepository);
        MessageService messageService = new BasicMessageService(messageRepository);

        // 3. 사용자 생성 테스트
        System.out.println("=== 사용자 생성 ===");
        User user1 = userService.create("홍길동", "hong@test.com", "pass1234", "010-1234-5678", "서울시 강남구");
        User user2 = userService.create("이순신", "lee@test.com", "secure4567", "010-2222-3333", "부산시 해운대구");

        System.out.println("생성된 사용자 1: " + user1);
        System.out.println("생성된 사용자 2: " + user2);

        // 4. 채널 생성 테스트
        System.out.println("\n=== 채널 생성 ===");
        Channel studyChannel = channelService.create(Channel.ChannelType.PRIVATE, "스터디 그룹", "자바 스터디를 위한 채널");
        Channel freeChat = channelService.create(Channel.ChannelType.PUBLIC, "잡담방", "자유롭게 이야기하는 공간");

        System.out.println("생성된 채널 1: " + studyChannel);
        System.out.println("생성된 채널 2: " + freeChat);

        // 5. 메시지 생성 테스트
        System.out.println("\n=== 메시지 생성 ===");
        Message message1 = messageService.create("안녕하세요! 자바 스터디 모집합니다.", studyChannel.getId(), user1.getId());
        Message message2 = messageService.create("스터디 참여하고 싶어요!", studyChannel.getId(), user2.getId());
        Message message3 = messageService.create("반갑습니다!", freeChat.getId(), user1.getId());

        System.out.println("메시지 1: " + message1);
        System.out.println("메시지 2: " + message2);
        System.out.println("메시지 3: " + message3);

        // 6. 데이터 조회 테스트
        System.out.println("\n=== 모든 사용자 조회 ===");
        List<User> allUsers = userService.findAll();
        allUsers.forEach(System.out::println);

        System.out.println("\n=== 모든 채널 조회 ===");
        List<Channel> allChannels = channelService.findAll();
        allChannels.forEach(System.out::println);

        System.out.println("\n=== 모든 메시지 조회 ===");
        List<Message> allMessages = messageService.findAll();
        allMessages.forEach(System.out::println);

        // 7. 사용자 업데이트 테스트
        System.out.println("\n=== 사용자 업데이트 ===");
        userService.update(user1.getId(), "phoneNumber", "010-9999-8888");
        System.out.println("업데이트된 사용자: " + userService.find(user1.getId()));

        // 8. 메시지 업데이트 테스트
        System.out.println("\n=== 메시지 업데이트 ===");
        messageService.update(message1.getId(), "자바 스터디 인원 모집 완료되었습니다.");
        System.out.println("업데이트된 메시지: " + messageService.find(message1.getId()));

        // 9. 데이터 삭제 테스트
        System.out.println("\n=== 데이터 삭제 ===");
        userService.delete(user2.getId());
        channelService.delete(freeChat.getId());
        messageService.delete(message3.getId());

        System.out.println("사용자 삭제 완료: " + user2.getId());
        System.out.println("채널 삭제 완료: " + freeChat.getId());
        System.out.println("메시지 삭제 완료: " + message3.getId());

        // 10. 최종 데이터 출력
        System.out.println("\n=== 최종 남아있는 데이터 ===");
        System.out.println("남아있는 사용자 목록:");
        userService.findAll().forEach(System.out::println);
        System.out.println("남아있는 채널 목록:");
        channelService.findAll().forEach(System.out::println);
        System.out.println("남아있는 메시지 목록:");
        messageService.findAll().forEach(System.out::println);
    }
}
