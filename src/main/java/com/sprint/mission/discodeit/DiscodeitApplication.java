package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);

        UserDto user = setupUser(userService);
        ChannelDto channel = setupChannel(channelService);

        messageCreateTest(messageService, channel, user);
    }

    private static UserDto setupUser(UserService userService) {
        UserStatus defaultStatus = new UserStatus(UUID.randomUUID(), Instant.now());
        UserDto newUser = new UserDto(
                UUID.randomUUID(),
                "woody",
                "woody@codeit.com",
                "woody1234",
                defaultStatus
        );
        return userService.createUser(newUser);
    }

    private static ChannelDto setupChannel(ChannelService channelService) {
        ChannelDto newChannel = new ChannelDto(
                UUID.randomUUID(),
                "공지",
                "공지 채널입니다.",
                ChannelType.PUBLIC,
                Instant.now(),
                Collections.emptyList()
        );
        return channelService.createChannel(newChannel);
    }

    private static void messageCreateTest(MessageService messageService, ChannelDto channel, UserDto user) {
        MessageDto newMessage = new MessageDto(
                UUID.randomUUID(),
                "안녕하세요. 환영합니다!",
                channel.getId(),
                user.getId()
        );

        MessageDto createdMessage = messageService.createMessage(newMessage);

        System.out.println("메시지 생성 완료:");
        System.out.println("  메시지 ID      : " + createdMessage.getId());
        System.out.println("  내용           : " + createdMessage.getContent());
        System.out.println("  채널 ID        : " + createdMessage.getChannelId());
        System.out.println("  작성자 ID      : " + createdMessage.getAuthorId());
    }
}
