package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.ChannelMessageService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.test.AuthTest;
import com.sprint.mission.discodeit.test.ChannelTest;
import com.sprint.mission.discodeit.test.MessageTest;
import com.sprint.mission.discodeit.test.UserTest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {

    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        /**
         * User Test
         */

        // 필요한 인스턴스 생성
        UserService userService = (UserService) context.getBean("basicUserService");

        // 유저 생성
        UUID user1Id = UserTest.setUpUser(userService);
        UUID user2Id = UserTest.setUpUser(userService);
        UUID user3Id = UserTest.setUpUser(userService);

        // 유저 정보 변경
        UserTest.updateUser(user1Id, userService);

        // 유저 삭제
        UserTest.deleteUser(user3Id, userService);


        /**
         * Auth Test
         */

        // 필요한 인스턴스 생성
        AuthService authService = (AuthService) context.getBean("basicAuthService");
        AuthTest.loginTest(authService);


        /**
         * Channel Test
         */

        // 필요한 인스턴스 생성
        ChannelService channelService = (ChannelService) context.getBean("basicChannelService");
        ChannelMessageService channelMessageService = (ChannelMessageService) context.getBean("basicChannelMessageService");
        ChannelTest channelTest = new ChannelTest(userService, channelService, channelMessageService);

        // 채널 생성
        UUID channel1Id = channelTest.setUpPublicChannel(user1Id);
        UUID channel2Id = channelTest.setUpPublicChannel(user1Id);

        // 채널 정보 변경
        channelTest.updateChannel(channel1Id);

        // 채널 멤버 변경
        channelTest.addMember(channel1Id, user2Id);
        channelTest.deleteMember(channel1Id, user2Id);

        // 채널 삭제
        channelTest.deleteChannel(channel2Id, user1Id);


        /**
         * Message Test
         */

        // 필요한 인스턴스 생성
        MessageService messageService = (MessageService) context.getBean("basicMessageService");
        MessageTest messageTest = new MessageTest(messageService, channelService, userService);

        // 메시지 생성
        UUID message1Id = messageTest.setUpMessage(channel1Id, user1Id);

        // 메시지 수정
        messageTest.updateMessage(message1Id);

        // 메시지 삭제
        messageTest.deleteMessage(message1Id, channel1Id);
    }

}