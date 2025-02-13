package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.jcf.*;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.service.basic.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public UserRepository userRepository() {
        return new JCFUserRepository();
    }

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
        return new BasicUserStatusService(userStatusRepository(), userRepository());
    }

    @Bean
    public ReadStatusService readStatusService() {
        return new BasicReadStatusService(readStatusRepository(), channelRepository(), userRepository());
    }

    @Bean
    public BinaryContentService binaryContentService() {
        return new BasicBinaryContentService(binaryContentRepository());
    }

    @Bean
    public UserService userService() {
        return new BasicUserService(userRepository(), binaryContentService());
    }
    //todo 순환참조 해결하기 - message <-> channel
    @Bean
    public MessageService messageService(){
        return new BasicMessageService(messageRepository(), userService(), channelService(), binaryContentService())
    }

    @Bean
    public ChannelService channelService() {
        return new BasicChannelService(channelRepository(), userRepository(), messageService(), );
    }

}
