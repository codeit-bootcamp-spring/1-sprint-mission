package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.Optional;

@SpringBootApplication
public class DiscodeitApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);

        User user = setupUser(userService);
        Channel channel = setupChannel(channelService, user);
        messageCreateTest(messageService, channel, user);
    }

    static User setupUser(UserService userService) {
        UserCreateRequest request = new UserCreateRequest("SG", "SG@codeit.com", "SG12341234");
        User user = userService.create(request, Optional.empty());
        return user;
    }

    static Channel setupChannel(ChannelService channelService, User admin) {
        PublicChannelCreateRequest request = new PublicChannelCreateRequest("공지", admin.getId());
        Channel channel = channelService.create(request);
        return channel;
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User writer) {
        MessageCreateRequest request = new MessageCreateRequest(channel.getId(), writer.getId(), "안녕하세요.");
        Message message = messageService.create(request, new ArrayList<>());
        System.out.println("메시지 생성: " + message.getId());
    }

}