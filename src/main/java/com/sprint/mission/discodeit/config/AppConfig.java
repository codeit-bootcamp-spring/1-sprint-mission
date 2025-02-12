package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import org.springframework.context.annotation.Bean;

public class AppConfig {
    @Bean
    public UserService userService() {
        return new BasicUserService(new FileUserRepository("tmp/user.ser"));
    }

    @Bean
    public ChannelService channelService() {
        return new BasicChannelService(new FileChannelRepository("tmp/channel.ser"));
    }

    @Bean
    public MessageService messageService() {
        return new BasicMessageService(new FileMessageRepository("tmp/message.ser"));
    }
}