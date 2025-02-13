package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.config.AppConfig;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


@SpringBootApplication
public class DiscodeitApplication {
    public static void main(String[] args) {
        //SpringApplication.run(DiscodeitApplication.class, args);

        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class);

        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);



    }
}

