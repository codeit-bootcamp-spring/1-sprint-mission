package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.factory.FactoryProvider;
import com.sprint.mission.discodeit.factory.ServiceFactory;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileNotFoundException;
import java.util.*;

@SpringBootApplication
public class DiscodeitApplication {
    public static void main(String[] args) {
        SpringApplication.run(DiscodeitApplication.class, args);
    }
}

static User setupUser(UserService userService) {
    return userService.createUser(new User("user01", "woody", "woody@codeit.com", "woody1234"));
}

static Channel setupChannel(ChannelService channelService) {
    return channelService.createChannel(new Channel("General", "공지", new HashMap<>(), new ArrayList<>(), ChannelType.GROUP));
}

static void messageCreateTest(MessageService messageService, Channel channel, User author) {
    Message message = messageService.createMessage(new Message("안녕하세요.", author, channel));
    System.out.println("메시지 생성: " + message.getId());
}

public static void main(String[] args) throws FileNotFoundException {
    // 실행 시점에 모드 선택: "JCF", "FILE", 또는 "BASIC"
    String mode = "BASIC"; // 원하는 모드로 변경 가능

    // FactoryProvider 초기화
    FactoryProvider.initialize(mode);

    // FactoryProvider를 통해 ServiceFactory 가져오기
    ServiceFactory serviceFactory = FactoryProvider.getInstance().getServiceFactory();

    // 서비스 초기화
    UserService userService = serviceFactory.createUserService();
    ChannelService channelService = serviceFactory.createChannelService();
    MessageService messageService = serviceFactory.createMessageService();

    // 테스트 실행
    System.out.println("=== 서비스 테스트 시작 ===");

    try {
        // 1. 사용자 생성 및 확인
        User user = setupUser(userService);
        System.out.println("생성된 사용자: " + user);

        // 2. 채널 생성 및 확인
        Channel channel = setupChannel(channelService);
        channelService.addParticipantToChannel(channel.getId(),user.getId());
        System.out.println("생성된 채널: " + channel);

        // 3. 메시지 생성 및 확인
        messageCreateTest(messageService, channel, user);

        System.out.println("=== 서비스 테스트 완료 ===");
    } catch (Exception e) {
        System.err.println("테스트 중 오류 발생: " + e.getMessage());
        e.printStackTrace();
    }
}
