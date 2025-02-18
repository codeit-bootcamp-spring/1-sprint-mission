package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.AuthDTO;
import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.dto.MessageDTO;
import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.enums.ChannelType;
import com.sprint.mission.discodeit.enums.RegionCode;
import com.sprint.mission.discodeit.enums.UserType;
import com.sprint.mission.discodeit.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@SpringBootApplication
public class DiscodeitApplication {

    // 코드잇 sprint3 베이스 코드 참고하여 생성
    static Long setupUser(UserService userService) {
        Long userId = userService.create(
                UserDTO.request.builder()
                        .userName("name")
                        .email("mail@mail.com")
                        .phone("010-0001-0002")
                        .password("!00password")
                        .userType(UserType.COMMON)
                        .nickname("nickname")
                        .regionCode(RegionCode.KR)
                        .build(),
                null);

        log.info("User created with ID: {}", userId);
        log.info("User : {}", userService.find(userId));

        boolean isUpdated = userService.update(
                UserDTO.update.builder()
                        .id(userId)
                        .userReqDTO(UserDTO.request.builder().userName("USERNAME").build())
                        .build()
        );
        log.info("User Update Success: {}", isUpdated);
        log.info("UserList\n{}", userService.findAll());

        return userId;
    }

    static Long setupUser2(UserService userService) {
        Long userId = userService.create(
                UserDTO.request.builder()
                        .userName("name2")
                        .email("mail2@mail.com")
                        .phone("010-0201-0002")
                        .password("!20password")
                        .userType(UserType.COMMON)
                        .nickname("USERUSER")
                        .regionCode(RegionCode.KR)
                        .build(),
                null);

        log.info("User created with ID: {}", userId);
        log.info("User : {}", userService.find(userId));
        return userId;
    }

    // 코드잇 sprint3 베이스 코드 참고하여 생성
    static Long setupChannel(ChannelService channelService, UUID owner) {
        Long channelId = channelService.createPublicChannel(ChannelDTO.request.builder()
                .owner(owner)
                .serverName("serverName1")
                .description("description!!!")
                .channelType(ChannelType.PUBLIC)
                .recent(Instant.now())
                .build()
        );
        log.info("Channel created with ID: {}", channelId);
        log.info("Channel : {}", channelService.find(channelId));
        return channelId;
    }

    static Long setupPrivateChannel(ChannelService channelService, UUID owner, UUID user2) {
        Long channelId = channelService.createPrivateChannel(ChannelDTO.request.builder()
                .owner(owner)
                .serverName("serverName1")
                .description("description!!!")
                .channelType(ChannelType.PRIVATE)
                .recent(Instant.now())
                .members(List.of(owner, user2))
                .build()
        );
        log.info("Private Channel created with ID: {}", channelId);
        log.info("Private Channel : {}", channelService.find(channelId));
        return channelId;
    }

    // 코드잇 sprint3 베이스 코드 참고하여 생성
    static void messageCreateTest(MessageService messageService, UUID channel, UUID author) {
        Long msgId = messageService.create(MessageDTO.request.builder()
                .author(author)
                .channel(channel)
                .content("CONTENTS")
                .build());
        log.info("Message created with ID: {}", msgId);
        log.info("Message : {}", messageService.find(msgId));
    }

    public static void main(String[] args) {
//        SpringApplication.run(DiscodeitApplication.class, args);
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);
        UserStatusService userStatusService = context.getBean(UserStatusService.class);
        ReadStatusService readStatusService = context.getBean(ReadStatusService.class);
        BinaryContentService binaryContentService = context.getBean(BinaryContentService.class);
        AuthService authService = context.getBean(AuthService.class);

        Long userId = setupUser(userService);
        Long userId2 = setupUser2(userService);
        Long channel = setupChannel(channelService, userService.find(userId).uuid());
        Long privateChannel = setupPrivateChannel(channelService, userService.find(userId).uuid(), userService.find(userId2).uuid());
        messageCreateTest(messageService, channelService.find(channel).uuid(), userService.find(userId2).uuid());

        log.info("User: {}", userService.findAll());
        log.info("UserStatus: {}", userStatusService.findAll());
        log.info("Channel: {}", channelService.findAllByUserId(userService.find(userId2).uuid()));
        log.info("ReadStatus: {}", readStatusService.findAllByUserId(userService.find(userId2).uuid()));
        log.info("Message: {}", messageService.findAllByChannelId(channelService.find(channel).uuid()));

        log.info("Login: {}", authService.login(AuthDTO.loginReq.builder().username("USERNAME").password("!00password").build()));

    }
}
