package com.sprint.mission;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelFindDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {
    static UserDto setupUser(UserService userService) {
        return userService.createUser(new UserDto("woody", "woodyPassword", "woody@naver.com"));
    }

    static ChannelDto setupChannel(ChannelService channelService) {
        return channelService.create(new ChannelDto("공지", ChannelType.PUBLIC));
    }

    static void messageCreateTest(MessageService messageService, ChannelDto channelDto, UserDto author) {
        UUID messageId = messageService.create(new MessageDto(null, author.id(), channelDto.id(), null, null, "안녕", null));
        Message message = messageService.findById(messageId);
        System.out.println("메시지 생성: " + message.getId());
    }

    public static void main(String[] args) {
        // 서비스 초기화
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);

        // 셋업
        UserDto userDto = setupUser(userService);
        ChannelDto channel = setupChannel(channelService);
        // 테스트
        messageCreateTest(messageService, channel, userDto);
    }
}

