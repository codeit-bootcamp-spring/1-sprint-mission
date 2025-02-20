package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.interfacepac.ChannelService;
import com.sprint.mission.discodeit.service.interfacepac.MessageService;
import com.sprint.mission.discodeit.service.interfacepac.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.Optional;

@SpringBootApplication
public class DiscodeitApplication {
    public static void main(String[] args) {
        SpringApplication.run(DiscodeitApplication.class, args);
    }
}