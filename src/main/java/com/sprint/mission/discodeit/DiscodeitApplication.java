package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.view.ConsoleView;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {

    static UserResponse setupUser(UserService userService, ConsoleView consoleView) {
        UserCreateRequest request = new UserCreateRequest("testUser", "email@email.com", "password123");
        UserResponse user = userService.createUser(request);
        consoleView.displayUser(user);
        return user;
    }

    static Channel setupChannel(ChannelService channelService, ConsoleView consoleView) {
        Channel channel = channelService.createChannel("testChannel", "first channel");
        consoleView.displayChannel(channel, null);
        return channel;
    }

    static void messageCreateTest(MessageService messageService, Channel channel, UserResponse user, ConsoleView consoleView) {
        Message message = messageService.createMessage(user.id(), channel.getId(), "테스트 메시지입니다!");
        if (message != null) {
            consoleView.displayMessage(message, channel.getChannelName(), user);
        } else {
            consoleView.displayError("메시지 전송 실패");
        }
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);
        ConsoleView consoleView = context.getBean(ConsoleView.class);

        consoleView.displaySuccess("=== 테스트 시작 ===");

        UserResponse testUser = setupUser(userService, consoleView);
        Channel testChannel = setupChannel(channelService, consoleView);
        messageCreateTest(messageService, testChannel, testUser, consoleView);

        consoleView.displaySuccess("=== 테스트 종료 ===");
    }
}

