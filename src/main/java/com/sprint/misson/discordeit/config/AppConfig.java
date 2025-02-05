package com.sprint.misson.discordeit.config;

import com.sprint.misson.discordeit.repository.*;
import com.sprint.misson.discordeit.repository.file.*;
import com.sprint.misson.discordeit.repository.jcf.*;
import com.sprint.misson.discordeit.service.*;
import com.sprint.misson.discordeit.service.basic.BasicBinaryContentService;
import com.sprint.misson.discordeit.service.basic.BasicReadStatusService;
import com.sprint.misson.discordeit.service.basic.BasicUserService;
import com.sprint.misson.discordeit.service.basic.BasicUserStatusService;
import com.sprint.misson.discordeit.service.file.FileChannelService;
import com.sprint.misson.discordeit.service.file.FileMessageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public UserRepository userRepository() {return new JCFUserRepository();}

    @Bean
    public ChannelRepository channelRepository() {
        return new JCFChannelRepository();
    }

    @Bean
    public MessageRepository messageRepository() {
        return new JCFMessageRepository();
    }

    @Bean
    public UserStatusRepository userStatusRepository() {
        return new JCFUserStatusRepository();
    }

    @Bean
    public ReadStatusRepository readStatusRepository() {
        return new JCFReadStatusRepository();
    }

    @Bean
    public BinaryContentRepository binaryContentRepository() {
        return new JCFBinaryContentRepository();
    }

    @Bean
    public UserStatusService userStatusService() {
        return new BasicUserStatusService(userStatusRepository());
    }

    @Bean
    public ReadStatusService readStatusService() {
        return new BasicReadStatusService(readStatusRepository());
    }

    @Bean
    public BinaryContentService binaryContentService() {
        return new BasicBinaryContentService(binaryContentRepository());
    }

    @Bean
    public UserService userService() {
        return new BasicUserService(userRepository(), userStatusService());
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
