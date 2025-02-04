package com.sprint.misson.discordeit.config;

import com.sprint.misson.discordeit.repository.ChannelRepository;
import com.sprint.misson.discordeit.repository.MessageRepository;
import com.sprint.misson.discordeit.repository.UserRepository;
import com.sprint.misson.discordeit.repository.file.FileChannelRepository;
import com.sprint.misson.discordeit.repository.file.FileMessageRepository;
import com.sprint.misson.discordeit.repository.file.FileUserRepository;
import com.sprint.misson.discordeit.service.ChannelService;
import com.sprint.misson.discordeit.service.MessageService;
import com.sprint.misson.discordeit.service.UserService;
import com.sprint.misson.discordeit.service.basic.BasicUserService;
import com.sprint.misson.discordeit.service.file.FileChannelService;
import com.sprint.misson.discordeit.service.file.FileMessageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public UserRepository userRepository() {
        return new FileUserRepository();
    }

    @Bean
    public ChannelRepository channelRepository() {
        return new FileChannelRepository();
    }

    @Bean
    public MessageRepository messageRepository() {
        return new FileMessageRepository();
    }

    @Bean
    public UserService userService() {
        return new BasicUserService(userRepository());
    }

    @Bean
    public ChannelService channelService() {
        return new FileChannelService(userService());
    }

    @Bean
    public MessageService messageService() {
        return new FileMessageService(userService(), channelService());
    }
}
