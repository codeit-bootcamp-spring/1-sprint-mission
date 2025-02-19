package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);

        User user = setupUser(userService);
        Channel channel = setupChannel(channelService, user);
        channelService.joinChannel(channel.getId(), user);
        messageCreateTest(messageService,channel,user);

    }

    static User setupUser(UserService userService) {
        User user = userService.create(
                UserDTO.createDTO.builder()
                        .userName("SG")
                        .email("SG@codeit.com")
                        .password("SG123123")
                        .build());
        return user;
    }

    static Channel setupChannel(ChannelService channelService, User admin) {
        Channel channel = channelService.create(ChannelType.PUBLIC, "공지채널", admin);
        return channel;
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        Message message = messageService.create("안녕하세요.", channel.getId(), author.getId());
        System.out.println("메시지 생성: " + message.getId());
    }

}
