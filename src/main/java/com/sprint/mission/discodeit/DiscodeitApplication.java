package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);


        //서비스 초기화
        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);

        //셋업 및 테스트
        User user = setUpUser(userService);
        Channel channel = setUpChannel(channelService, user);
        messageCreateTest(messageService,channel,user);
    }

    private static void messageCreateTest(MessageService messageService, Channel channel, User user) {
        Message message = new Message(user, channel, "Hello");
        messageService.createMessage(message);
        System.out.println(message);
    }

    private static User setUpUser(UserService userService) {
        User user = new User("John Par", "John1234@naver.com", "1234");
        userService.createUser(user.getUserName(), user.getUserEmail(), user.getPassword());
        System.out.println(user);
        return user;
    }

    private static Channel setUpChannel(ChannelService channelService, User user) {
        Channel channel = new Channel("General", user);
        channelService.createChannel(channel);
        System.out.println(channel);
        return channel;
    }

}