package com.sprint.mission;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.UUID;
@SpringBootApplication
public class DiscodeitApplication {
    static UserDto setupUser(UserService userService) {
        return userService.createUser(new UserDto("woody", "woody@naver.com"));
    }

    static Channel setupChannel(ChannelService channelService) {
        UUID channelId = channelService.createChannel("공지");
        return channelService.getChannel(channelId);
    }

    static void messageCreateTest(MessageService messageService, Channel channel, UserDto author) {
        UUID messageId = messageService.createMessage(channel.getId(), "안녕");
        Message message = messageService.getMessage(messageId);
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
        Channel channel = setupChannel(channelService);
        // 테스트
        messageCreateTest(messageService, channel, userDto);
    }
}

